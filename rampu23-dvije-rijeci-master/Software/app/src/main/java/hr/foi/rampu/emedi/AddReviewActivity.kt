package hr.foi.rampu.emedi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.entities.Review
import hr.foi.rampu.emedi.helpers.TextSizeUtility

class AddReviewActivity : AppCompatActivity() {
    private lateinit var ratingBar: RatingBar
    private lateinit var editTextReview: EditText
    private lateinit var submitButton: Button
    private lateinit var currentDoctor: Doctor
    private lateinit var textSizeUtility : TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_review)

        textSizeUtility = TextSizeUtility.getInstance()

        ratingBar = findViewById(R.id.ratingBar)
        editTextReview = findViewById(R.id.editTextReview)
        submitButton = findViewById(R.id.submitButton)

        currentDoctor = intent.getParcelableExtra<Doctor>("doctor")!!

        submitButton.setOnClickListener {
            addReviewAndDisplayAllReviews()
        }
        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()
    }

    private fun addReviewAndDisplayAllReviews() {
        val userGrade = ratingBar.rating.toInt()
        val userDescription = editTextReview.text.toString()

        val userReview = Review(Review.getNewReviewId(), userGrade, userDescription, currentDoctor.id)
        Review.addReview(userReview)

        val intent = Intent(this, AllReviewsActivity::class.java)
        intent.putExtra("doctor", currentDoctor)
        startActivity(intent)
    }

    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllButtons(findViewById(R.id.submitButton))
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.submitButton), position)

    }
}
