package hr.foi.rampu.emedi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.entities.User
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DoctorInformationActivity : AppCompatActivity() {

    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_information)

        val receivedDoctor = intent.getParcelableExtra<Doctor>("doctor")

        val btnBooking: Button = findViewById(R.id.btn_booking)
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("doctor", receivedDoctor)
            startActivity(intent)
        }

        val btnCheckReviews: Button = findViewById(R.id.btn_reviews)
        btnCheckReviews.setOnClickListener {
            checkReviews()
        }

        val btnViewBookings: Button = findViewById(R.id.btn_view_bookings)
        btnViewBookings.setOnClickListener {
            viewBookings()
        }
        val btnShareDoctorsInformation: ImageButton = findViewById(R.id.btn_share_information)
        btnShareDoctorsInformation.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            val textToShare = "${receivedDoctor?.name} ${receivedDoctor?.surname}\n" +
                    "Specialization: ${receivedDoctor?.specialization}\n" +
                    "Years of experience: ${receivedDoctor?.yearsEmployed}\n" +
                    "E-mail:${receivedDoctor?.email}\n" +
                    "Phone Number: ${receivedDoctor?.telephone}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
            startActivity(Intent.createChooser(shareIntent, "Share doctors information"))
        }

        // inicijaliziram textview-ove
        val tvNameSurname = findViewById<TextView>(R.id.tv_dynamic_name_surname)
        val tvSpecialization = findViewById<TextView>(R.id.tv_dynamic_specialization)
        val tvYearsEmployed = findViewById<TextView>(R.id.tv_dynamic_years)
        val tvJobDescription = findViewById<TextView>(R.id.tv_dynamic_job_description)
        val tvClinicName = findViewById<TextView>(R.id.tv_dynamic_clinic_name)
        val tvAddress = findViewById<TextView>(R.id.tv_dynamic_address)
        val tvEmail = findViewById<TextView>(R.id.tv_dynamic_email)
        val tvTelephone = findViewById<TextView>(R.id.tv_dynamic_telephone)

        // učitavam u text-view
        tvNameSurname.text = receivedDoctor?.name.orEmpty() + " " + receivedDoctor?.surname.orEmpty()
        tvSpecialization.text = receivedDoctor?.specialization.orEmpty()
        tvYearsEmployed.text = "${receivedDoctor?.yearsEmployed ?: 0}"
        tvJobDescription.text = receivedDoctor?.jobDescription.orEmpty()
        tvClinicName.text = receivedDoctor?.clinicName.orEmpty()
        tvAddress.text = receivedDoctor?.address.orEmpty()
        tvEmail.text = receivedDoctor?.email.orEmpty()
        tvTelephone.text = receivedDoctor?.telephone.orEmpty()


        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()
    }

    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()

        textSizeUtility.registerAllTextViews(
            findViewById(R.id.tv_static_specialization),
            findViewById(R.id.tv_static_telephone),
            findViewById(R.id.tv_static_about),
            findViewById(R.id.tv_static_address),
            findViewById(R.id.tv_static_contact),
            findViewById(R.id.tv_static_clinic_name),
            findViewById(R.id.tv_static_email),
            findViewById(R.id.tv_static_job_description),
            findViewById(R.id.tv_static_location),
            findViewById(R.id.tv_static_years_employed),
            findViewById(R.id.tv_dynamic_name_surname),
            findViewById(R.id.tv_dynamic_specialization),
            findViewById(R.id.tv_dynamic_years),
            findViewById(R.id.tv_dynamic_address),
            findViewById(R.id.tv_dynamic_job_description),
            findViewById(R.id.tv_dynamic_clinic_name),
            findViewById(R.id.tv_dynamic_email),
            findViewById(R.id.tv_dynamic_telephone),
        )


        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_years_employed), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_email), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_location), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_about), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_address), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_job_description), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_clinic_name), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_contact), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_telephone), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_static_specialization), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_telephone), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_email), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_address), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_years), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_clinic_name), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_job_description), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_name_surname), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_dynamic_specialization), position)


        textSizeUtility.registerAllButtons(
            findViewById(R.id.btn_booking),
            findViewById(R.id.btn_reviews),
            findViewById(R.id.btn_view_bookings)
        )


        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btn_booking), position)
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btn_reviews), position)
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btn_view_bookings), position)

    }

    private fun checkReviews() {
        val receivedDoctor = intent.getParcelableExtra<Doctor>("doctor")
        val intent = Intent(this, AllReviewsActivity::class.java)
        intent.putExtra("doctor", receivedDoctor)
        startActivity(intent)
    }

    private fun viewBookings() {
        val receivedDoctor = intent.getParcelableExtra<Doctor>("doctor")
        val intent = Intent(this, BookingsActivity::class.java)
        intent.putExtra("doctor", receivedDoctor)
        startActivity(intent)
    }

}
