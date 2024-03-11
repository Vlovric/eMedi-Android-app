package hr.foi.rampu.emedi

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import hr.foi.rampu.emedi.adapters.BookingReasonAdapter
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.database.AppointmentsDAO
import hr.foi.rampu.emedi.entities.Appointment
import hr.foi.rampu.emedi.entities.BookingReason
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.entities.NotificationReceiver
import hr.foi.rampu.emedi.helpers.NotificationHelper
import hr.foi.rampu.emedi.helpers.UserSession
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookingsActivity : AppCompatActivity() {
    private var selectedDate: Calendar = Calendar.getInstance()
    private var dateChosen: Boolean = false
    private var selectedStartTime: String = "00:00"
    private var selectedEndTime: String = "00:00"

    private var timesList: MutableList<List<String>> = mutableListOf()

    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)

    private lateinit var listBookings: ListView
    private lateinit var currentDoctor: Doctor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        currentDoctor = intent.getParcelableExtra<Doctor>("doctor")!!

        updateBookingData()
    }

    private fun updateBookingData() {
        listBookings = findViewById(R.id.list_bookings)
        val allBookings = AppDatabase.getInstance().getBookingReasonsDao().getBookingsForDoctorAndUser(currentDoctor.id, UserSession.loggedUser.id)
        val adapter = BookingReasonAdapter(this, this,  allBookings)
        listBookings.adapter = adapter
    }

    fun showNewAppointmentDialog(booking: BookingReason) {
        val newAppointmentDialog = LayoutInflater
            .from(this)
            .inflate(R.layout.new_appointment_dialog, null)

        selectedDate = Calendar.getInstance()
        dateChosen = false
        selectedStartTime = "00:00"
        selectedEndTime = "00:00"
        timesList = mutableListOf()

        AlertDialog.Builder(this)
            .setView(newAppointmentDialog)
            .setPositiveButton(getString(R.string.save_appointment_time)) { _, _ ->
                if (!dateChosen) {
                    Toast.makeText(this, getString(R.string.date_is_not_chosen), Toast.LENGTH_SHORT).show()
                } else if (selectedStartTime == "00:00" || selectedEndTime == "00:00") {
                    Toast.makeText(this, getString(R.string.time_is_not_chosen), Toast.LENGTH_SHORT).show()
                } else {
                    var isOverlapping: Boolean = false
                    timesList.forEach { time ->
                        if (checkOverlap(selectedStartTime, selectedEndTime, time[0], time[1])) {
                            isOverlapping = true
                        }
                    }

                    if (isOverlapping) {
                        Toast.makeText(this, "This time is overlapping.", Toast.LENGTH_SHORT).show()
                    } else {
                        addNewAppointment(booking)
                        scheduleNotificationsForAppointments(selectedStartTime)

                    }
                }
            }
            .setNegativeButton(getString(R.string.close)) { dialog, which ->
                dialog.dismiss()
            }
            .show()

        activateDateTimeListeners(newAppointmentDialog)
    }

    private fun addNewAppointment(booking: BookingReason) {
        val appointmentsDAO = AppDatabase.getInstance().getAppointmentsDao()
        val newId: Int = getNewAppointmentId(appointmentsDAO)

        val newAppointment = Appointment(
            newId,
            selectedDate.time,
            Date(0, 0, 0, getStringHour(selectedStartTime), getStringMinute(selectedStartTime)),
            Date(0, 0, 0, getStringHour(selectedEndTime), getStringMinute(selectedEndTime)),
            currentDoctor.id,
            UserSession.loggedUser.id,
            booking.id
        )

        appointmentsDAO.insertAppointment(newAppointment)
        updateBookingData()
    }

    private fun getNewAppointmentId(appointmentsDAO: AppointmentsDAO): Int {
        val allAppointments = appointmentsDAO.getAllAppointments()
        var newId = allAppointments.count()

        for (appointment in allAppointments) {
            if (appointment.id > newId) {
                newId = appointment.id
            }
        }

        return ++newId
    }

    private fun activateDateTimeListeners(view: View) {
        val dateSelection = view.findViewById<EditText>(R.id.et_appointment_date)
        val startTimeSelection = view.findViewById<EditText>(R.id.et_appointment_start_time)
        val endTimeSelection = view.findViewById<EditText>(R.id.et_appointment_end_time)

        val topView = view

        checkDateAvalibility(view, selectedDate, true)
        checkDoctorAvalibility(view, selectedDate, true)

        dateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val newDialog = DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedDate.set(year, monthOfYear, dayOfMonth)
                        dateSelection.setText(sdfDate.format(selectedDate.time).toString())
                        dateChosen = true

                        checkDateAvalibility(topView, selectedDate)
                        checkDoctorAvalibility(topView, selectedDate)
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
                )
                newDialog.show()

                view.clearFocus()
            }
        }

        startTimeSelection.setOnFocusChangeListener {view, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    view.context,
                    { _, hourOfDay, minute ->
                        selectedStartTime = getTimeString(hourOfDay, minute)
                        startTimeSelection.setText(selectedStartTime)

                        selectedEndTime = addToTime(selectedStartTime, 30)
                        endTimeSelection.setText(selectedEndTime)
                    },
                    getStringHour(selectedStartTime),
                    getStringMinute(selectedStartTime),
                    true
                ).show()
                view.clearFocus()
            }
        }

        endTimeSelection.setOnFocusChangeListener {view, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    view.context,
                    { _, hourOfDay, minute ->
                        if (compareTimes(selectedStartTime, getTimeString(hourOfDay, minute)) != 1) {
                            Toast.makeText(view.context,
                                getString(R.string.end_time_must_be_after_start_time), Toast.LENGTH_SHORT).show()
                            selectedEndTime = "00:00"
                            endTimeSelection.setText("")
                        } else {
                            selectedEndTime = getTimeString(hourOfDay, minute)
                            endTimeSelection.setText(selectedEndTime)
                        }
                    },
                    getStringHour(selectedEndTime),
                    getStringMinute(selectedEndTime),
                    true
                ).show()
                view.clearFocus()
            }
        }
    }

    private fun checkDateAvalibility(view: View, date: Calendar, firstSet: Boolean = false) {
        val tvExistingAppointmentsMessage = view.findViewById<TextView>(R.id.tv_existing_appointments_message)
        val tvExistingAppointments = view.findViewById<TextView>(R.id.tv_existing_appointments)

        tvExistingAppointmentsMessage.visibility = View.GONE
        tvExistingAppointments.visibility = View.GONE

        if (firstSet) {
            return
        }

        val allCurrentUserAppointments = AppDatabase.getInstance().getAppointmentsDao().getAppointmentsForUser(UserSession.loggedUser.id)
        if (allCurrentUserAppointments.isEmpty()) {
            return
        }

        var appointmentMessage: String = ""
        var first: Boolean = true

        for (appointment in allCurrentUserAppointments) {
            if ((appointment.appointmentDate.year + 1900) == date.get(Calendar.YEAR)
                && appointment.appointmentDate.month == date.get(Calendar.MONTH)
                && appointment.appointmentDate.date == date.get(Calendar.DAY_OF_MONTH)) {

                if (!first) appointmentMessage += "\n"
                appointmentMessage += "- " + getTimeString(
                    appointment.appointmentStartTime.hours,
                    appointment.appointmentStartTime.minutes
                )
                appointmentMessage += " to " + getTimeString(
                    appointment.appointmentEndTime.hours,
                    appointment.appointmentEndTime.minutes
                )
                appointmentMessage += " with " + appointment.doctor.name + " " + appointment.doctor.surname

                first = false
                tvExistingAppointmentsMessage.visibility = View.VISIBLE
                tvExistingAppointments.visibility = View.VISIBLE

                timesList.add(listOf(
                    String.format("%02d:%02d", appointment.appointmentStartTime.hours, appointment.appointmentStartTime.minutes),
                    String.format("%02d:%02d", appointment.appointmentEndTime.hours, appointment.appointmentEndTime.minutes)
                ))
            }
        }

        tvExistingAppointments.text = appointmentMessage
    }

    private fun checkDoctorAvalibility(view: View, date: Calendar, firstSet: Boolean = false) {
        val tvExistingDoctorAppointmentsMessage = view.findViewById<TextView>(R.id.tv_existing_doctor_appointments_message)
        val tvExistingDoctorAppointments = view.findViewById<TextView>(R.id.tv_existing_doctor_appointments)

        tvExistingDoctorAppointmentsMessage.visibility = View.GONE
        tvExistingDoctorAppointments.visibility = View.GONE

        if (firstSet) {
            return
        }

        val allDoctorAppointments = AppDatabase.getInstance().getAppointmentsDao().getAppointmentsForDoctor(currentDoctor.id)
        if (allDoctorAppointments.isEmpty()) {
            return
        }

        var appointmentMessage: String = ""
        var first: Boolean = true

        for (appointment in allDoctorAppointments) {
            if ((appointment.appointmentDate.year + 1900) == date.get(Calendar.YEAR)
                && appointment.appointmentDate.month == date.get(Calendar.MONTH)
                && appointment.appointmentDate.date == date.get(Calendar.DAY_OF_MONTH)) {

                if (!first) appointmentMessage += "\n"
                appointmentMessage += "- from " + getTimeString(
                    appointment.appointmentStartTime.hours,
                    appointment.appointmentStartTime.minutes
                )
                appointmentMessage += " to " + getTimeString(
                    appointment.appointmentEndTime.hours,
                    appointment.appointmentEndTime.minutes
                )

                first = false
                tvExistingDoctorAppointmentsMessage.visibility = View.VISIBLE
                tvExistingDoctorAppointments.visibility = View.VISIBLE

                timesList.add(listOf(
                    String.format("%02d:%02d", appointment.appointmentStartTime.hours, appointment.appointmentStartTime.minutes),
                    String.format("%02d:%02d", appointment.appointmentEndTime.hours, appointment.appointmentEndTime.minutes)
                ))
            }
        }

        tvExistingDoctorAppointments.text = appointmentMessage
    }

    private fun getTimeString(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }

    private fun getStringHour(time: String): Int {
        return time.substring(0, 2).toInt()
    }

    private fun getStringMinute(time: String): Int {
        return time.substring(3, 5).toInt()
    }

    private fun addToTime(time: String, minutesAdd: Int): String {
        var hours: Int = getStringHour(time)
        var minutes: Int = getStringMinute(time)

        minutes += minutesAdd;
        hours += (minutes / 60)
        minutes %= 60
        hours %= 24

        return getTimeString(hours, minutes)
    }

    private fun compareTimes(time1: String, time2: String): Int {
        val hours1: Int = getStringHour(time1)
        val hours2: Int = getStringHour(time2)
        val minutes1: Int = getStringMinute(time1)
        val minutes2: Int = getStringMinute(time2)

        if (hours1 > hours2) {
            return -1
        } else if (hours1 < hours2) {
            return 1
        } else {
            if (minutes1 > minutes2) {
                return -1
            } else if (minutes1 < minutes2) {
                return 1
            } else {
                return 0
            }
        }

        return 0
    }

    private fun scheduleNotificationsForAppointments(appointmentStartTime: String) {
        val notificationTimeMillis = convertTimeStringToMillis(selectedDate, appointmentStartTime) - 3600000

        scheduleNotification(notificationTimeMillis)
    }

    private fun scheduleNotification(notificationTimeMillis: Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent)
    }


    private fun convertTimeStringToMillis(date: Calendar, timeString: String): Long {
        val parsedTime = SimpleDateFormat("HH:mm", Locale.GERMANY).parse(timeString) ?: Date()
        val timeCalendar = Calendar.getInstance().apply {
            time = parsedTime
            set(Calendar.YEAR, date.get(Calendar.YEAR))
            set(Calendar.MONTH, date.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return timeCalendar.timeInMillis
    }




    private fun checkOverlap(start1: String, end1: String, start2: String, end2: String): Boolean {
        Log.i("TIME", "Start 1: " + start1)
        Log.i("TIME", "End 1: " + end1)
        Log.i("TIME", "Start 2: " + start2)
        Log.i("TIME", "End 3: " + end2)

        return (((compareTimes(start1, end2) == 1) && (compareTimes(end1, start2) == -1)) || ((compareTimes(start2, end1) == 1) && (compareTimes(end2, start1) == -1)))
    }
}