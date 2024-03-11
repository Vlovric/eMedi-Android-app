package hr.foi.rampu.emedi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import hr.foi.rampu.emedi.helpers.MockDataUser
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import hr.foi.rampu.emedi.helpers.UserSession

class LoginActivity : AppCompatActivity() {
    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        }
    }


    private lateinit var textSizeUtility: TextSizeUtility
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UserSession.loggedIn = false
        UserSession.loggedUser = MockDataUser.getDummyUser()

        val linkRegistration = findViewById<TextView>(R.id.link_register)
        val loginButton = findViewById<Button>(R.id.btn_login)

        val usernameField = findViewById<EditText>(R.id.et_username_edit)
        val passwordField = findViewById<EditText>(R.id.et_password_edit)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val loggedInUser = MockDataUser.findUserByCredentials(username, password)


            if (loggedInUser != null) {
                UserSession.loggedIn = true
                UserSession.loggedUser = loggedInUser
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (username.isEmpty() && password.isEmpty())
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT)
                        .show()
                else {
                    Toast.makeText(this, "Enter all login information!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        linkRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)

        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        changeTextSize()
    }
    private fun changeTextSize() {
        val position = sharedPreferences.getInt("selectedPosition", 1)
        TextSizeUtility.initialize(this)
        textSizeUtility = TextSizeUtility.getInstance()

        textSizeUtility.registerAllTextViews(findViewById(R.id.link_register),findViewById(R.id.tv_account))
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.link_register), position)
        textSizeUtility.registerTextViewStyle(this, findViewById(R.id.tv_account), position)


        textSizeUtility.registerAllButtons(findViewById(R.id.btn_login))
        textSizeUtility.registerButtonStyle(this, findViewById(R.id.btn_login), position)

    }
}