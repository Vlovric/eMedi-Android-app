package hr.foi.rampu.emedi

import ReviewAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings.TextSize
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import hr.foi.rampu.emedi.entities.Doctor
import hr.foi.rampu.emedi.entities.Review
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import org.w3c.dom.Text

class AllReviewsActivity : AppCompatActivity() {
    private lateinit var listViewReviews: ListView
    private lateinit var currentDoctor: Doctor
    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_reviews)

        currentDoctor = intent.getParcelableExtra<Doctor>("doctor")!!
        if (currentDoctor != null) {
            Log.i("DOBIVEN DOKTOR", currentDoctor.id.toString())
        }

        textSizeUtility = TextSizeUtility.getInstance()
        val allReviews = Review.getReviewsForDoctor(currentDoctor)

        listViewReviews = findViewById(R.id.listViewReviews)

        val adapter = ReviewAdapter(this, allReviews)
        listViewReviews.adapter = adapter
        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun addReviewBtnClicked(view: View) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra("doctor", currentDoctor)
        startActivity(intent)
    }

    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllButtons(findViewById(R.id.addReviewButton))
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.addReviewButton), position)

        textSizeUtility.registerAllButtons(findViewById(R.id.btnBack))
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btnBack), position)

    }

}
