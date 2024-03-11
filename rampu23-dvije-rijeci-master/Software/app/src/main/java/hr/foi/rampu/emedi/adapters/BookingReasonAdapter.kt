package hr.foi.rampu.emedi.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import hr.foi.rampu.emedi.BookingsActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Appointment
import hr.foi.rampu.emedi.entities.BookingReason
import hr.foi.rampu.emedi.entities.Doctor
import java.text.SimpleDateFormat
import java.util.Locale

class BookingReasonAdapter(private val parentActivity: BookingsActivity, private val context: Context, private val bookingReasons: List<BookingReason>): BaseAdapter() {
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    override fun getCount(): Int {
        return bookingReasons.size
    }

    override fun getItem(position: Int): Any {
        return bookingReasons[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rowView = inflater.inflate(R.layout.booking, parent, false)

        val tvSymptoms = rowView.findViewById<TextView>(R.id.tv_symptoms)
        val tvDuration = rowView.findViewById<TextView>(R.id.tv_duration)
        val tvHistory = rowView.findViewById<TextView>(R.id.tv_history)
        val tvUrgency = rowView.findViewById<TextView>(R.id.tv_urgency)
        val tvAdditional = rowView.findViewById<TextView>(R.id.tv_additional)
        val tvAdditionalLabel = rowView.findViewById<TextView>(R.id.tv_additional_label)

        val bookingReason = getItem(position) as BookingReason

        tvSymptoms.text = bookingReason.symptoms
        tvDuration.text = bookingReason.duration
        tvHistory.text = bookingReason.history
        tvUrgency.text = bookingReason.urgency
        if (bookingReason.additional == "") {
            tvAdditionalLabel.visibility = View.GONE
        } else {
            tvAdditional.text = bookingReason.additional
        }

        val btnAddAppointment = rowView.findViewById<Button>(R.id.btn_choose_date_and_time)

        val appointmentForBooking = AppDatabase.getInstance().getAppointmentsDao().getAppointmentForBooking(bookingReason.id)
        if (appointmentForBooking.isEmpty()) {
            rowView.findViewById<TextView>(R.id.tv_booking_appointment).visibility = View.GONE
        } else {
            rowView.findViewById<TextView>(R.id.tv_booking_message).visibility = View.GONE
            btnAddAppointment.visibility = View.GONE

            val currentAppointment: Appointment = appointmentForBooking[0]
            var appointmentText = "Appointment: " + sdfDate.format(currentAppointment.appointmentDate)
            appointmentText += " at " + sdfTime.format(currentAppointment.appointmentStartTime)

            rowView.findViewById<TextView>(R.id.tv_booking_appointment).text = appointmentText
        }

        btnAddAppointment.setOnClickListener {
            parentActivity.showNewAppointmentDialog(bookingReason)
        }

        return rowView
    }
}