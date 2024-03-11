package hr.foi.rampu.emedi.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rampu.emedi.LoginActivity
import hr.foi.rampu.emedi.MainActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.User
import hr.foi.rampu.emedi.helpers.ProfileChangeHelper
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import hr.foi.rampu.emedi.helpers.UserSession
import hr.foi.rampu.emedi.helpers.UserSession.loggedUser
import kotlinx.coroutines.Dispatchers.Main
import java.util.Date

enum class ProfileState {
    Viewing,
    Editing
}

class ProfileFragment : Fragment() {

    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var changeTextView : View

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeTextView = inflater.inflate(R.layout.appointment, container, false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profileState = ProfileState.Viewing
        val profileChangeHelper = ProfileChangeHelper(view, loggedUser)

        val logoutButton: Button = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            if (profileState == ProfileState.Editing) {
                Toast.makeText(view.context, "Please, save changes first!", Toast.LENGTH_LONG)
                    .show()
            } else {
                val intent = Intent(activity, LoginActivity::class.java)
                activity?.startActivity(intent)
            }
        }
        val deleteButton: Button = view.findViewById(R.id.btn_delete)
        deleteButton.setOnClickListener {
            if (profileState == ProfileState.Editing) {
                Toast.makeText(view.context, "Please, save changes first!", Toast.LENGTH_LONG)
                    .show()
            } else {
                AppDatabase.getInstance().getUsersDao().deleteUser(UserSession.loggedUser)
                val intent = Intent(activity, LoginActivity::class.java)
                activity?.startActivity(intent)
            }
        }
     //   changeTextSize(changeTextView)
    }

    /*    private fun changeTextSize(view : View) {

        sharedPreferences = requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(requireContext())
        textSizeUtility = TextSizeUtility.getInstance()


          textSizeUtility.registerAllTextViews(
            view.findViewById(R.id.tv_profile_fragment_text),
            view.findViewById(R.id.tv_name_and_surname),
            view.findViewById(R.id.tv_username),
            view.findViewById(R.id.tv_birth_date),
            view.findViewById(R.id.tv_email),
            view.findViewById(R.id.tv_change_email),
            view.findViewById(R.id.tv_telephone_number),
            view.findViewById(R.id.tv_change_telephone_number),
            view.findViewById(R.id.tv_address),
            view.findViewById(R.id.tv_change_address)
            )
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_profile_fragment_text), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_name_and_surname), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_birth_date), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_email), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_change_email), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_telephone_number), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_change_telephone_number), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_address), position)
        textSizeUtility.registerTextViewStyle(requireContext(), view.findViewById(R.id.tv_change_address), position)



        textSizeUtility.registerAllButtons(
            view.findViewById(R.id.btn_logout),
            view.findViewById(R.id.btn_delete)
        )
        textSizeUtility.registerButtonStyle(requireContext(), view.findViewById(R.id.btn_logout), position)
        textSizeUtility.registerButtonStyle(requireContext(), view.findViewById(R.id.btn_delete), position)
    } */
}