package hr.foi.rampu.emedi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.helpers.TextSizeUtility
class SingleReviewActivity : AppCompatActivity() {
    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_review)

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()
    }

    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllTextViews(findViewById(R.id.textViewDescription))
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.textViewDescription), position)

    }
}