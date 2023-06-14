package com.example.gomommy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import androidx.core.widget.addTextChangedListener

class LoginAccount : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    //private lateinit var forgotPasswordTextView: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account)

        usernameEditText = findViewById(R.id.createUsernameEditText)
        passwordEditText = findViewById(R.id.createPasswordEditText)
        //forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)
        createAccountButton = findViewById(R.id.loginButton)
        loginButton = findViewById(R.id.createAccountButton)
        loginButton.isEnabled = false // Initially disable the sign-up yesButton

        // Add text change listeners to the username and password EditText fields
        usernameEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        passwordEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform sign-up logic here

            // Redirect to the desired activity
            val intent = Intent(this, MomExperience::class.java)
            startActivity(intent)
            finish()
        }

        createAccountButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateSignUpButtonState() {
        val isUsernameFilled = usernameEditText.text.isNotEmpty()
        val isPasswordFilled = passwordEditText.text.isNotEmpty()
        this.loginButton.isEnabled = isUsernameFilled && isPasswordFilled
        if(this.loginButton.isEnabled){
            this.loginButton.setBackgroundResource(R.drawable.log_button)
            this.loginButton.setTextColor(Color.parseColor("#FE5065"))
        }else {
            this.loginButton.setBackgroundResource(R.drawable.disable_button)
            this.loginButton.setTextColor(Color.parseColor("#D2D1D1"))
        }
        val showPasswordToggleButton = findViewById<ToggleButton>(R.id.showPasswordToggleButton)
        val passwordEditText = findViewById<EditText>(R.id.createPasswordEditText)

        showPasswordToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordEditText.transformationMethod = null
            } else {
                // Hide password
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        // Set cursor position to the end of the password field
        passwordEditText.setSelection(passwordEditText.text.length)
    }
}