package hr.foi.rampu.emedi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.adapters.AppointmentAdapter
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import hr.foi.rampu.emedi.helpers.UserSession

class AppointmentsFragment : Fragment() {
    private lateinit var listViewAppointments: ListView

    private lateinit var myView: View

    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var changeTextView : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeTextView = inflater.inflate(R.layout.appointment, container, false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myView = view
        updateAppointmentData()

        changeTextSize(changeTextView)
    }

    override fun onResume() {
        updateAppointmentData()
        super.onResume()
    }

    private fun updateAppointmentData() {
        var appointmentList = AppDatabase.getInstance().getAppointmentsDao().getAppointmentsForUser(
            UserSession.loggedUser.id)
        listViewAppointments = myView.findViewById(R.id.lv_all_appointments)

        val adapter = AppointmentAdapter(myView.context, appointmentList)
        listViewAppointments.adapter = adapter
    }

    private fun changeTextSize(view : View) {

        sharedPreferences = requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(requireContext())
        textSizeUtility = TextSizeUtility.getInstance()


        textSizeUtility.registerAllTextViews(
            view.findViewById(R.id.tv_appointment_doctor),
            view.findViewById(R.id.tv_appdate),
            view.findViewById(R.id.tv_appointment_date_and_time),
            view.findViewById(R.id.tv_appsymptoms),
            view.findViewById(R.id.tv_appointment_symptoms),
            view.findViewById(R.id.tv_appointment_already_done)
        )
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appointment_doctor), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appdate), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appointment_date_and_time), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appsymptoms), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appointment_symptoms), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_appointment_already_done), position)


    }
}