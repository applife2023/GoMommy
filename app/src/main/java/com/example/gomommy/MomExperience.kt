package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.gomommy.databinding.ActivityMomExperienceBinding

class MomExperience : AppCompatActivity() {
    private lateinit var binding: ActivityMomExperienceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMomExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            yesButton.setOnClickListener {
                val intent = Intent(this@MomExperience, BirthYearPicker::class.java)
                startActivity(intent)
                finish()
            }

            noButton.setOnClickListener {
                val intent = Intent(this@MomExperience, BirthYearPicker::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}