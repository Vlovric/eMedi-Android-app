package hr.foi.rampu.emedi.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import hr.foi.rampu.emedi.MainActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.helpers.TextSizeUtility

class SettingsFragment : Fragment() {

    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fontSpinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        TextSizeUtility.initialize(requireContext())
        textSizeUtility = TextSizeUtility.getInstance()

        textSizeUtility.registerAllTextViews(view.findViewById(R.id.tv_FontSize))


        sharedPreferences = requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        val seekBarFontSize: SeekBar = view.findViewById(R.id.fontSizeSeekBar)
        val btnBack: Button = view.findViewById(R.id.btnBack)
        val darkTheme: CheckBox = view.findViewById(R.id.cbdarkTheme)
        val savedFontSize = sharedPreferences.getFloat("fontSize", 12f)
        seekBarFontSize.progress = (savedFontSize - 12f).toInt()

        seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                val newSize = 12f + progress.toFloat()
                textSizeUtility.setTextSize(newSize)

                with(sharedPreferences.edit()) {
                    putFloat("fontSize", newSize)
                    apply()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })



        fontSpinner = view.findViewById(R.id.fontSpinner)

        fontSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                with(sharedPreferences.edit()) {
                    putInt("selectedPosition", position)
                    apply()
                }
                textSizeUtility.registerTextViewStyle(requireActivity(), view.findViewById(R.id.tv_FontSize), position)
                textSizeUtility.registerButtonStyle(requireActivity(), view.findViewById(R.id.btnBack), position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        btnBack.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        darkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        changeTextSize(view)
        return view
    }

    private fun changeTextSize(view : View) {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(requireContext())
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllTextViews(view.findViewById(R.id.tv_FontSize))
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_FontSize), position)


        textSizeUtility.registerAllButtons(
            view.findViewById(R.id.btnBack),
            )
        textSizeUtility.registerButtonStyle(requireContext(), view.findViewById(R.id.btnBack), position)

    }


}
