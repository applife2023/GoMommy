package com.example.gomommy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth

class CreateAccount : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText:EditText
    private lateinit var createAccountButton: Button
    private lateinit var logInTextView: TextView
    //private lateinit var forgotPasswordTextView: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        firebaseAuth = FirebaseAuth.getInstance()
        usernameEditText = findViewById(R.id.createUsernameEditText)
        passwordEditText = findViewById(R.id.createPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        logInTextView = findViewById(R.id.logInTextView)
        createAccountButton = findViewById(R.id.createAccountButton)

        // Add text change listeners to the username and password EditText fields
        usernameEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        passwordEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        createAccountButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform sign-up logic here
            if (username.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        // Redirect to the desired activity
                        val intent = Intent(this, MomExperience::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please fill the fields", Toast.LENGTH_SHORT).show()
            }
        }

        logInTextView.setOnClickListener{
            val intent = Intent(this, LoginAccount::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateSignUpButtonState() {
        val isUsernameFilled = usernameEditText.text.isNotEmpty()
        val isPasswordFilled = passwordEditText.text.isNotEmpty()
        val isConfirmPasswordFilled = confirmPasswordEditText.text.isNotEmpty()
        val isPasswordMatched = passwordEditText.text.toString() == confirmPasswordEditText.text.toString()

        this.createAccountButton.isEnabled = isUsernameFilled && isPasswordFilled && isConfirmPasswordFilled && isPasswordMatched
        if (this.createAccountButton.isEnabled) {
            this.createAccountButton.setBackgroundResource(R.drawable.log_button)
            this.createAccountButton.setTextColor(Color.parseColor("#FE5065"))
        } else {
            this.createAccountButton.setBackgroundResource(R.drawable.disable_button)
            this.createAccountButton.setTextColor(Color.parseColor("#D2D1D1"))
        }
    }
}