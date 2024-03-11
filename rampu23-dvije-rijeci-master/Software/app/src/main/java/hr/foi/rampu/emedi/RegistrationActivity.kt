package hr.foi.rampu.emedi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import hr.foi.rampu.emedi.entities.User
import hr.foi.rampu.emedi.helpers.InputCheckHelper
import hr.foi.rampu.emedi.helpers.InputCheckHelper.afterTextChanged
import hr.foi.rampu.emedi.helpers.MockDataUser
import hr.foi.rampu.emedi.helpers.RegistrationActivityHelper
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrationActivity : AppCompatActivity() {
    private var registrationHelper : RegistrationActivityHelper? = null
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy.", Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationHelper = RegistrationActivityHelper(this)
        setContentView(R.layout.activity_registration)

        // Support za Toolbar
        val toolBar = findViewById<Toolbar>(R.id.toolbar_login)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        // EditText objekti za unos
        val birthDate = findViewById<EditText>(R.id.et_birthdate_edit)
        val firstName = findViewById<EditText>(R.id.et_firstname_edit)
        val lastName = findViewById<EditText>(R.id.et_lastname_edit)
        val email = findViewById<EditText>(R.id.et_email_edit)
        val phoneNumber = findViewById<EditText>(R.id.et_phone_edit)
        val address = findViewById<EditText>(R.id.et_address_edit)
        val username = findViewById<EditText>(R.id.et_register_username_edit)
        val password = findViewById<EditText>(R.id.et_register_password_edit)
        // Textview objekti za greške
        val firstNameErr = findViewById<TextView>(R.id.tv_firstname)
        val lastNameErr = findViewById<TextView>(R.id.tv_lastname)
        val addressErr = findViewById<TextView>(R.id.tv_adrress)
        val emailErr = findViewById<TextView>(R.id.tv_email)
        val phoneErr = findViewById<TextView>(R.id.tv_phone)
        val usernameErr = findViewById<TextView>(R.id.tv_username)
        val passwordErr = findViewById<TextView>(R.id.tv_password_edit)
        // Provjere unosa
        var firstNameValid = false
        var lastNameValid = false
        var addressValid = false
        var emailValid = false
        var phoneNumberValid = false
        var usernameValid = false
        var passwordValid = false


        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener{
            if(!firstNameValid || !lastNameValid || !addressValid || !emailValid || !phoneNumberValid || ! usernameValid || !passwordValid){
                return@setOnClickListener
            }

            val firstNameButton = firstName.text.toString()
            val lastNameButton = lastName.text.toString()
            val emailButton = email.text.toString()
            val phoneNumberButton = phoneNumber.text.toString()
            val addressButton = address.text.toString()
            val usernameButton = username.text.toString()
            val passwordButton = password.text.toString()


            val newUser = User(MockDataUser.getNewUserId(),
                firstNameButton,
                lastNameButton,
                sdfDate.parse(birthDate.text.toString()),
                emailButton,
                phoneNumberButton,
                addressButton,
                usernameButton,
                passwordButton
            )

            MockDataUser.insertUser(newUser)
            Log.i("USERADDED", "User count: ${MockDataUser.getUserCount()}")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        birthDate.setOnFocusChangeListener{ view, hasFocus ->
            if(hasFocus) {
                registrationHelper!!.showDatePickerDialog()
                view.clearFocus()
            }
        }
        firstName.afterTextChanged {
            val errorMessage = InputCheckHelper.checkFirstname(firstName.text.toString())
            if(errorMessage != ""){
                firstNameErr.visibility = View.VISIBLE
                firstNameErr.text = errorMessage
                firstNameValid = false
            } else {
                firstNameErr.visibility = View.INVISIBLE
                firstNameValid = true
            }
        }
        lastName.afterTextChanged {
            val errorMessage = InputCheckHelper.checkLastname(lastName.text.toString())
            if (errorMessage != "") {
                lastNameErr.visibility = View.VISIBLE
                lastNameErr.text = errorMessage
                lastNameValid = false
            } else {
                lastNameErr.visibility = View.INVISIBLE
                lastNameValid = true
            }
        }
        address.afterTextChanged {
            val errorMessage = InputCheckHelper.checkAddress(address.text.toString())
            if (errorMessage != "") {
                addressErr.visibility = View.VISIBLE
                addressErr.text = errorMessage
                addressValid = false
            } else {
                addressErr.visibility = View.INVISIBLE
                addressValid = true
            }
        }
        email.afterTextChanged {
            val errorMessage = InputCheckHelper.emailAddressCheck(email.text.toString())
            if(errorMessage != ""){
                emailErr.visibility = View.VISIBLE
                emailErr.text = errorMessage
                emailValid = false
            }else{
                emailErr.visibility = View.INVISIBLE
                emailValid = true
            }

        }
        phoneNumber.afterTextChanged {
            val errorMessage = InputCheckHelper.telephoneNumberCheck(phoneNumber.text.toString())
            if(errorMessage != ""){
                phoneErr.visibility = View.VISIBLE
                phoneErr.text = errorMessage
                phoneNumberValid = false
            }else{
                phoneErr.visibility = View.INVISIBLE
                phoneNumberValid = true
            }

        }
        username.afterTextChanged {
            val errorMessage = InputCheckHelper.checkUsername(username.text.toString())
            if(errorMessage != ""){
                usernameErr.visibility = View.VISIBLE
                usernameErr.text = errorMessage
                usernameValid = false
            }else{
                usernameErr.visibility = View.INVISIBLE
                usernameValid = true
            }

        }
        password.afterTextChanged {
            val errorMessage = InputCheckHelper.checkPassword(password.text.toString())
            if(errorMessage != ""){
                passwordErr.visibility = View.VISIBLE
                passwordErr.text = errorMessage
                Log.i("PORUKA", errorMessage)
                passwordValid = false
            }else{
                passwordErr.visibility = View.INVISIBLE
                passwordValid = true
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Deprecated je zato što treba naći neku noviju metodu
        return true
    }
}