package hr.foi.rampu.emedi.helpers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import hr.foi.rampu.emedi.LoginActivity
import hr.foi.rampu.emedi.MainActivity
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.entities.User
import hr.foi.rampu.emedi.fragments.ProfileFragment
import hr.foi.rampu.emedi.fragments.ProfileState
import hr.foi.rampu.emedi.helpers.InputCheckHelper.emailAddressCheck
import hr.foi.rampu.emedi.helpers.InputCheckHelper.telephoneNumberCheck
import kotlinx.coroutines.Dispatchers.Main
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileChangeHelper(private val view: View, private val loggedUser: User) {
    private val nameAndSurname: TextView = view.findViewById(R.id.tv_name_and_surname)
    private val editingMessage: TextView = view.findViewById(R.id.tv_editing_text)
    private val allowEditingButton: ImageView = view.findViewById(R.id.iv_edit_profile)
    private val saveChangesButton: ImageView = view.findViewById(R.id.iv_save_changes)

    private val usernameText: TextView = view.findViewById(R.id.tv_username)
    private val birthDateText: TextView = view.findViewById(R.id.tv_birth_date)
    private val emailAddressText: TextView = view.findViewById(R.id.tv_email)
    private val telephoneNumberText: TextView = view.findViewById(R.id.tv_telephone_number)
    private val addressText: TextView = view.findViewById(R.id.tv_address)

    private val emailAddressChangeText: TextView = view.findViewById(R.id.tv_change_email)
    private val telephoneNumberChangeText: TextView = view.findViewById(R.id.tv_change_telephone_number)
    private val addressChangeText: TextView = view.findViewById(R.id.tv_change_address)
    
    private val defaultTextColor: Int = addressText.currentTextColor
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)

    private var user = loggedUser

    var profileState: ProfileState = ProfileState.Viewing

    init {
        adjustElementsByState(profileState)
        writeUserDataToTextViews()
        loadUser()

        allowEditingButton.setOnClickListener {
            loadUser()
            profileState = ProfileState.Editing
            adjustElementsByState(profileState)
            writeUserDataToTextViews()
        }
        saveChangesButton.setOnClickListener {
            profileState = ProfileState.Viewing
            adjustElementsByState(profileState)
            writeUserDataToTextViews()
            saveUser()
        }

        emailAddressChangeText.setOnClickListener {
            buildDialog(user.email, "electronic mail",
                { newEmail ->
                    user.email = newEmail
                    adjustElementsByState(profileState)
                    writeUserDataToTextViews()
                }, { stringToCheck ->
                    emailAddressCheck(stringToCheck)
                }
            )
        }
        telephoneNumberChangeText.setOnClickListener {
            buildDialog(user.telephoneNumber, "phone number",
                { newPhoneNumber ->
                    user.telephoneNumber = newPhoneNumber
                    adjustElementsByState(profileState)
                    writeUserDataToTextViews()
                }, { stringToCheck ->
                    telephoneNumberCheck(stringToCheck)
                }
            )
        }
        addressChangeText.setOnClickListener {
            buildDialog(user.address, "address",
                { newAddress ->
                    user.address = newAddress
                    adjustElementsByState(profileState)
                    writeUserDataToTextViews()
                }, { _ -> "" }
            )
        }
    }

    private fun buildDialog(text: String, propertyName: String, positiveAction: (String) -> Unit, errorCheck: (String) -> String) {
        val textChangeDialogView = LayoutInflater
            .from(view.context)
            .inflate(R.layout.text_change_dialog, null)

        val newTextField = textChangeDialogView.findViewById<EditText>(R.id.et_new_text)
        if (propertyName == "phone number") newTextField.inputType = InputType.TYPE_CLASS_NUMBER
        val errorMessage = textChangeDialogView.findViewById<TextView>(R.id.tv_error_message)

        newTextField.setText(text)
        errorMessage.text = ""
        var hasError = false
        var errorText = ""

        newTextField.afterTextChanged {
            errorText = errorCheck.invoke(it)
            Log.i("TEST", errorText)
            errorMessage.text = errorText
            hasError = errorText.isNotBlank()
            errorMessage.visibility = if (hasError) View.VISIBLE else View.INVISIBLE
        }

        AlertDialog.Builder(view.context)
            .setView(textChangeDialogView)
            .setTitle(view.context.getString(R.string.change_in_dialog, propertyName))
            .setPositiveButton("Save changes") { _, _ ->
                if (hasError) {
                    Snackbar.make(view, errorText, Snackbar.LENGTH_SHORT).show()
                } else {
                    positiveAction.invoke(newTextField.text.toString())
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()

    }

    private fun adjustElementsByState(state: ProfileState) {
        when (state) {
            ProfileState.Viewing -> {
                editingMessage.visibility = View.GONE
                allowEditingButton.visibility = View.VISIBLE
                saveChangesButton.visibility = View.GONE
                emailAddressChangeText.visibility = View.GONE
                telephoneNumberChangeText.visibility = View.GONE
                addressChangeText.visibility = View.GONE
            }
            else -> {
                editingMessage.visibility = View.VISIBLE
                allowEditingButton.visibility = View.GONE
                saveChangesButton.visibility = View.VISIBLE
                emailAddressChangeText.visibility = View.VISIBLE
                telephoneNumberChangeText.visibility = View.VISIBLE
                addressChangeText.visibility = View.VISIBLE
            }
        }
    }

    private fun writeUserDataToTextViews() {
        nameAndSurname.text = "${user.name} ${user.surname}"
        usernameText.text = user.username
        birthDateText.text = sdfDate.format(user.birthDate).toString()
        emailAddressText.text = user.email
        telephoneNumberText.text = user.telephoneNumber
        if (user.address.isBlank()) {
            addressText.text = view.context.getString(R.string.no_address)
            addressText.setTextColor(Color.parseColor("#989898"))
        } else {
            addressText.text = user.address
            addressText.setTextColor(defaultTextColor)
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    private fun loadUser(): User {
        return MockDataUser.findUserByCredentials(user.username, user.password)
    }

    private fun saveUser() {
        MockDataUser.insertUser(user)
        UserSession.loggedUser = MockDataUser.getUserById(user.id)
    }
}