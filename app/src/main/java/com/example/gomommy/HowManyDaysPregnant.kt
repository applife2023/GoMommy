package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HowManyDaysPregnant : AppCompatActivity() {
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_many_days_pregnant)

        btnContinue = findViewById(R.id.continueButton)
        btnContinue.setOnClickListener {
            val intent = Intent(this, OfficialWelcome::class.java)
            startActivity(intent)
            finish()
        }
    }
}