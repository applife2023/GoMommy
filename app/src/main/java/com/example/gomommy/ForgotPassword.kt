package com.example.gomommy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : ComponentActivity() {
    private lateinit var etPassword: EditText
    private lateinit var resetPassword: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etPassword = findViewById(R.id.resetEmailEditText)
        resetPassword = findViewById(R.id.resetPasswordButton)

        auth = FirebaseAuth.getInstance()

        resetPassword.setOnClickListener(){
            val sPassword = etPassword.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnCompleteListener {
                    Toast.makeText(this, "Please check your email", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}
