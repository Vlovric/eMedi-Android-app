package hr.foi.rampu.emedi.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import hr.foi.rampu.emedi.AppointmentDetailsActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Appointment
import hr.foi.rampu.emedi.entities.Review
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentAdapter(private val context: Context, private val appointments: List<Appointment>): BaseAdapter() {
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    override fun getCount(): Int {
        return appointments.size
    }

    override fun getItem(position: Int): Any {
        return appointments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.appointment, parent, false)

        val tvDoctor = rowView.findViewById<TextView>(R.id.tv_appointment_doctor)
        val tvDateAndTime = rowView.findViewById<TextView>(R.id.tv_appointment_date_and_time)
        val tvSymptoms = rowView.findViewById<TextView>(R.id.tv_appointment_symptoms)
        val tvAppointmentAlreadyDone = rowView.findViewById<TextView>(R.id.tv_appointment_already_done)
        val btnViewDetails = rowView.findViewById<Button>(R.id.btn_appt_details)

        val appointment = getItem(position) as Appointment
        val appDoctor = appointment.doctor
        var appBookingReason = appointment.bookingReason

        tvDoctor.text =
            "Appointment with ${appDoctor.name} ${appDoctor.surname} (${appDoctor.specialization})"
        tvDateAndTime.text =
            "${sdfDate.format(appointment.appointmentDate)} at ${sdfTime.format(appointment.appointmentStartTime)}"
        if (appBookingReason != null) {
            tvSymptoms.text = appBookingReason.symptoms
        }

        tvAppointmentAlreadyDone.visibility = View.GONE
        btnViewDetails.visibility = View.GONE

        if (Date().time > appointment.appointmentDate.time) {
            rowView.background = ContextCompat.getDrawable(context, R.drawable.rounded_background_gray)
            tvAppointmentAlreadyDone.visibility = View.VISIBLE
            btnViewDetails.visibility = View.VISIBLE

            btnViewDetails.setOnClickListener {
                val intent = Intent(context, AppointmentDetailsActivity::class.java)
                intent.putExtra("AppointmentId", appointment.id)
                context.startActivity(intent)
            }
        }

        return rowView
    }
}