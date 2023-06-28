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
import com.example.gomommy.databinding.ActivityLoginAccountBinding
import com.google.firebase.auth.FirebaseAuth

class LoginAccount : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginAccountBinding
    //private lateinit var forgotPasswordTextView: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()



        with(binding) {
            loginButton.isEnabled = false // Initially disable the sign-up yesButton

            // Add text change listeners to the username and password EditText fields
            loginUsernameEditText.addTextChangedListener { text ->
                updateSignUpButtonState()
            }

            loginPasswordEditText.addTextChangedListener { text ->
                updateSignUpButtonState()
            }

            loginButton.setOnClickListener {
                val username = loginUsernameEditText.text.toString()
                val password = loginPasswordEditText.text.toString()

                // Perform sign-up logic here
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Redirect to the desired activity
                                val intent = Intent(this@LoginAccount, Homepage::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginAccount, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this@LoginAccount, "Please fill the fields", Toast.LENGTH_SHORT).show()
                }

            }

            createAccountTextView.setOnClickListener {
                val intent = Intent(this@LoginAccount, CreateAccount::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun updateSignUpButtonState() {
        val isUsernameFilled = binding.loginUsernameEditText.text.isNotEmpty()
        val isPasswordFilled = binding.loginPasswordEditText.text.isNotEmpty()
        binding.loginButton.isEnabled = isUsernameFilled && isPasswordFilled
        if(binding.loginButton.isEnabled){
            binding.loginButton.setBackgroundResource(R.drawable.log_button)
            binding.loginButton.setTextColor(Color.parseColor("#FE5065"))
        }else {
            binding.loginButton.setBackgroundResource(R.drawable.disable_button)
            binding.loginButton.setTextColor(Color.parseColor("#D2D1D1"))
        }

        binding.showPasswordToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                binding.loginPasswordEditText.transformationMethod = null
            } else {
                // Hide password
                binding.loginPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        // Set cursor position to the end of the password field
        binding.loginPasswordEditText.setSelection(binding.loginPasswordEditText.text.length)
    }
}