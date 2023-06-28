package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar

class OfficialWelcome : AppCompatActivity() {
    private lateinit var btnContinue: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_official_welcome)

        btnContinue = findViewById(R.id.continueButton)
        btnContinue.setOnClickListener{
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }
    }
}