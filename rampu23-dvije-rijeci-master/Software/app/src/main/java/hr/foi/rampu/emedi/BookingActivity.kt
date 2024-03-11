package hr.foi.rampu.emedi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Appointment
import hr.foi.rampu.emedi.entities.BookingReason
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.helpers.NotificationHelper
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import hr.foi.rampu.emedi.helpers.UserSession

class BookingActivity : AppCompatActivity() {
    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()

        val warning = findViewById<TextView>(R.id.tv_warning)
        val doctorName = intent.getParcelableExtra<Doctor>("doctor")?.name
        val doctorSurname = intent.getParcelableExtra<Doctor>("doctor")?.surname

        val btnSendBooking: Button = findViewById(R.id.btn_sendBooking)
        btnSendBooking.setOnClickListener {
            val symptoms = findViewById<EditText>(R.id.et_symptoms).text.toString()
            val duration = findViewById<EditText>(R.id.et_duration).text.toString()
            val history = findViewById<EditText>(R.id.et_history).text.toString()
            val urgency = findViewById<EditText>(R.id.et_urgency).text.toString()
            val additional = findViewById<EditText>(R.id.et_additional_info).text.toString()

            if (symptoms.isNotEmpty() && duration.isNotEmpty() && history.isNotEmpty()
                && urgency.isNotEmpty()
            ) {
                val newBookingReason = intent.getParcelableExtra<Doctor>("doctor")?.let {
                    BookingReason(
                        id = getNewBookingReasonId(),
                        symptoms = symptoms,
                        duration = duration,
                        history = history,
                        urgency = urgency,
                        additional = additional,
                        doctorId = it.id,
                        userId = UserSession.loggedUser.id
                    )
                }

                if (newBookingReason != null) {
                    AppDatabase.getInstance().getBookingReasonsDao()
                        .insertBookingReason(newBookingReason)

                    Handler(Looper.getMainLooper()).postDelayed({
                        NotificationHelper.showNotification(
                            this,
                            "$doctorName $doctorSurname has accepted the reason for your visit."
                        )

                    }, 5000)

                    finish()
                } else {
                    warning.visibility = View.VISIBLE
                }
            } else {
                warning.visibility = View.VISIBLE
            }
        }

        // Your existing code...
    }

    private fun getNewBookingReasonId(): Int {
        val bookingReasonsDAO = AppDatabase.getInstance().getBookingReasonsDao()
        val allBookingReasons = bookingReasonsDAO.getAllBookingReasons()
        return allBookingReasons.size + 1
    }


    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()

        textSizeUtility.registerAllTextViews(findViewById(R.id.tv_warning))
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_warning), position)

        textSizeUtility.registerAllButtons(findViewById(R.id.btn_sendBooking))
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btn_sendBooking), position)
    }
}
