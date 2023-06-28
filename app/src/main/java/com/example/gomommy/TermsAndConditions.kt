package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gomommy.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditions : AppCompatActivity() {

    private  lateinit var binding: ActivityTermsAndConditionsBinding
    private lateinit var checkbox1: CheckBox
    private lateinit var checkbox2: CheckBox
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.checkbox1 = findViewById(R.id.checkbox1)
        this.checkbox2 = findViewById(R.id.checkbox2)
        this.button = findViewById(R.id.nextButton)

        with(binding) {
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
            nextButton.setOnClickListener {
                if (nextButton.isEnabled) {
                    // Perform the desired action or navigate to the desired activity
                    val intent = Intent(this@TermsAndConditions, LoginAccount::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }


    // Function to update the enabled state of the yesButton based on checkbox states
    private fun updateButtonEnabledState() {
        binding.nextButton.isEnabled = binding.checkbox1.isChecked && binding.checkbox2.isChecked
        if (binding.nextButton.isEnabled) {
            // Change yesButton background color when enabled
            binding.nextButton.setBackgroundResource(R.drawable.gradient_bg)
        } else {
            // Change yesButton background color when disabled
            binding.nextButton.setBackgroundResource(R.color.disabled_btn)
        }
    }
}
