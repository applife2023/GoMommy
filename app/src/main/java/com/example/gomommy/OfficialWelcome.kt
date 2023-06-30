package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import com.example.gomommy.databinding.ActivityOfficialWelcomeBinding

class OfficialWelcome : AppCompatActivity() {
    private lateinit var binding: ActivityOfficialWelcomeBinding
    private lateinit var btnContinue: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOfficialWelcomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            continueButton.setOnClickListener {
                val intent = Intent(this@OfficialWelcome, Homepage::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}