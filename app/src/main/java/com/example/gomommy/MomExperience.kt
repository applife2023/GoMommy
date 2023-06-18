package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MomExperience : AppCompatActivity() {
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mom_experience)

        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)

        yesButton.setOnClickListener{
            val intent = Intent(this, BirthYearPicker::class.java)
            startActivity(intent)
        }

        noButton.setOnClickListener{
            val intent = Intent(this, BirthYearPicker::class.java)
            startActivity(intent)
        }
    }
}