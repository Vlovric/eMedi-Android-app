// TextSizeUtility.kt
package hr.foi.rampu.emedi.helpers

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import hr.foi.rampu.emedi.R

class TextSizeUtility private constructor(private val context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("TextSizePrefs", Context.MODE_PRIVATE)

    private val allTextViews: MutableList<TextView> = mutableListOf()
    private val allButtons: MutableList<Button> = mutableListOf()

    // -------------------------- SIZE EDITOR --------------------------

    fun setTextSize(size: Float) {
        preferences.edit().putFloat("textSize", size).apply()
        applyTextSizeToViews()
    }

    fun registerAllTextViews(vararg textViews: TextView) {
        allTextViews.addAll(textViews)
        applyTextSizeToViews()
    }

    fun registerAllButtons(vararg buttons: Button) {
        allButtons.addAll(buttons)
        applyTextSizeToViews()
    }

    private fun applyTextSizeToViews() {
        for (textView in allTextViews) {
            applyTextSizeToView(textView)
        }
        for (button in allButtons) {
            applyTextSizeToView(button)
        }
    }

    private fun applyTextSizeToView(view: View) {
        if (view is TextView) {
            val textSize = preferences.getFloat("textSize", 12f)
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        }
    }
// -------------------------- STYLE EDITOR --------------------------
    fun registerTextViewStyle(context: Context, textView: TextView, position: Int) {
        applyFont(context, textView, position)
    }

    fun registerButtonStyle(context: Context, button: Button, position: Int) {
        applyFont(context, button, position)
    }

    private fun applyFont(context: Context, textView: TextView, position: Int) {
        val fontResourceId = getFontResourceId(position)
        val typeface = ResourcesCompat.getFont(context, fontResourceId)
        textView.typeface = typeface
    }

    private fun getFontResourceId(position: Int): Int {
        return when (position) {
            0 -> R.font.default_font
            1 -> R.font.jetbrainsmono
            2 -> R.font.dyslexie
            3 -> R.font.robotomono
            4 -> R.font.slabo
            5 -> R.font.titilliumweb
            else -> R.font.default_font
        }
    }

    companion object {
        private lateinit var instance: TextSizeUtility

        fun initialize(context: Context) {
            instance = TextSizeUtility(context)
        }

        fun getInstance(): TextSizeUtility {
            return instance
        }
    }
}
