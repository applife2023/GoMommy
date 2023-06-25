package com.example.gomommy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccount : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText:EditText
    private lateinit var createAccountButton: Button
    private lateinit var logInTextView: TextView
    //private lateinit var forgotPasswordTextView: Button
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        firebaseAuth = FirebaseAuth.getInstance()
        usernameEditText = findViewById(R.id.createUsernameEditText)
        passwordEditText = findViewById(R.id.createPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        logInTextView = findViewById(R.id.logInTextView)
        createAccountButton = findViewById(R.id.createAccountButton)

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        // Add text change listeners to the username and password EditText fields
        usernameEditText.addTextChangedListener {
            updateSignUpButtonState()
        }

        passwordEditText.addTextChangedListener {
            updateSignUpButtonState()
        }

        confirmPasswordEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }

        createAccountButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Perform sign-up logic here
            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password ==  confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Redirect to the desired activity
                            val intent = Intent(this, MomExperience::class.java)
                            saveUserEmailPassword(username,password)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "The password does not match!", Toast.LENGTH_SHORT).show()
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

    private fun saveUserEmailPassword(userEmail: String, userPassword: String){
        val userId = dbRef.push().key!!
        val user = userMommyModel( userId,userEmail,userPassword)

        val firebaseUser = firebaseAuth.currentUser?.uid
        println("User UID: $firebaseUser")

        if (firebaseUser != null) {
            dbRef.child(firebaseUser).child("Login-Credentials").setValue(user)
                .addOnCompleteListener{ Toast.makeText(this,"data stored sucessfully", Toast.LENGTH_LONG).show()
                }
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
        val showPasswordToggleButton = findViewById<ToggleButton>(R.id.showPasswordToggleButton)
        val passwordEditText = findViewById<EditText>(R.id.createPasswordEditText)
        val confirmShowPasswordToggleButton = findViewById<ToggleButton>(R.id.confirmShowPasswordToggleButton)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val passwordMismatchTextView = findViewById<TextView>(R.id.passwordMismatchTextView)

        passwordMismatchTextView.isVisible = !isPasswordMatched

        this.confirmPasswordEditText.isEnabled = isPasswordFilled
        if (this.confirmPasswordEditText.isEnabled) {
            this.confirmPasswordEditText.setBackgroundResource(R.drawable.custom_edit_text)
            this.confirmPasswordEditText.setTextColor(Color.parseColor("#FF000000"))
        } else {
            this.confirmPasswordEditText.setBackgroundResource(R.drawable.custom_edit_text_disabled)
            this.confirmPasswordEditText.setTextColor(Color.parseColor("#D2D1D1"))
        }

        showPasswordToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordEditText.transformationMethod = null
            } else {
                // Hide password
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        confirmShowPasswordToggleButton.isEnabled = isConfirmPasswordFilled
        confirmShowPasswordToggleButton.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // Show password
                confirmPasswordEditText.transformationMethod = null
            } else {
                // Hide password
                confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // Set cursor position to the end of the password field
        passwordEditText.setSelection(passwordEditText.text.length)
        confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
    }
}