package hr.foi.rampu.emedi.helpers

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrationActivityHelper(private val context : Context) {
    private val dateCalendar: Calendar = Calendar.getInstance()
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    fun showDatePickerDialog(){
        val dateSelection = (context as AppCompatActivity).findViewById<EditText>(R.id.et_birthdate_edit)
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dateCalendar.set(year, month, dayOfMonth)
                dateSelection.setText(sdfDate.format(dateCalendar.time))
            },
            dateCalendar.get(Calendar.YEAR),
            dateCalendar.get(Calendar.MONTH),
            dateCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }


}