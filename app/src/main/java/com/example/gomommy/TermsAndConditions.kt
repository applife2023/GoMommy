package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TermsAndConditions : AppCompatActivity() {

    private lateinit var checkbox1: CheckBox
    private lateinit var checkbox2: CheckBox
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        this.checkbox1 = findViewById(R.id.checkbox1)
        this.checkbox2 = findViewById(R.id.checkbox2)
        this.button = findViewById(R.id.nextButton)

        // Set a listener for checkbox1
        checkbox1.setOnCheckedChangeListener { _, _ ->
            updateButtonEnabledState()
        }

        // Set a listener for checkbox2
        checkbox2.setOnCheckedChangeListener { _, _ ->
            updateButtonEnabledState()
        }

        // Call the updateButtonEnabledState initially to set the initial state of the yesButton
        updateButtonEnabledState()

        // Set a click listener for the Next yesButton
        button.setOnClickListener {
            if (button.isEnabled) {
                // Perform the desired action or navigate to the desired activity
                val intent = Intent(this, LoginAccount::class.java)
                startActivity(intent)

                finish()
            }
        }
    }

    // Function to update the enabled state of the yesButton based on checkbox states
    private fun updateButtonEnabledState() {
        button.isEnabled = checkbox1.isChecked && checkbox2.isChecked
        if (button.isEnabled) {
            // Change yesButton background color when enabled
            button.setBackgroundResource(R.drawable.gradient_bg)
        } else {
            // Change yesButton background color when disabled
            button.setBackgroundResource(R.color.disabled_btn)
        }
    }
}
