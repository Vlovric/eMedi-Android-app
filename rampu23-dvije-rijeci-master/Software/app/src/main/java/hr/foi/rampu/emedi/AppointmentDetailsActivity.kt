package hr.foi.rampu.emedi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.helpers.AppointmentDetailHelper
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentDetailsActivity : AppCompatActivity() {
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)

        val appointmentId = intent.getSerializableExtra("AppointmentId") as Int

        val appDatabaseInst = AppDatabase.getInstance()

        val appointment = appDatabaseInst.getAppointmentsDao().getAppointmentById(appointmentId)
        var appointmentDetail = appDatabaseInst.getAppointmentDetailsDAO().getAppointmentDetailsForAppointment(appointmentId)

        val tbAppointmentDate = findViewById<TextView>(R.id.tv_appointment_date)
        val tbAppointmentDoctor = findViewById<TextView>(R.id.tv_appointment_doctor)
        val tbAppointmentDescription = findViewById<TextView>(R.id.tv_appointment_description)
        val tbAppointmentSymptoms = findViewById<TextView>(R.id.tv_appointment_symptoms)
        val tbAppointmentDiagnosis = findViewById<TextView>(R.id.tv_appointment_diagnosis)
        val tbAppointmentNextSteps = findViewById<TextView>(R.id.tv_appointment_next_steps)
        val tbAppointmentMedications = findViewById<TextView>(R.id.tv_appointment_medications)

        if (appointmentDetail.isEmpty()) {
            appointmentDetail = AppointmentDetailHelper.saveNewAppointmentDetail(appointmentId, appDatabaseInst.getAppointmentDetailsDAO())
        }

        tbAppointmentDate.text = appointment.appointmentDate.toString()
        tbAppointmentDoctor.text = appointment.doctor.name + " " + appointment.doctor.surname + " (" + appointment.doctor.specialization + ")"
        tbAppointmentSymptoms.text = "${sdfDate.format(appointment.appointmentDate)} at ${sdfTime.format(appointment.appointmentStartTime)}"

        tbAppointmentDescription.text = appointmentDetail[0].description
        tbAppointmentDiagnosis.text = appointmentDetail[0].diagnosis
        tbAppointmentNextSteps.text = appointmentDetail[0].nextSteps
        tbAppointmentMedications.text = appointmentDetail[0].medications
    }
}