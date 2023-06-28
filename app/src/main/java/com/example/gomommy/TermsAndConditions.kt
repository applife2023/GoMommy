package com.example.gomommy

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
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

        // Call the updateButtonEnabledState initially to set the initial state of the button
        updateButtonEnabledState()

        // Set a click listener for the button
        button.setOnClickListener {
            if (button.isEnabled) {
                // Perform the desired action or navigate to the desired activity
                val intent = Intent(this, LoginAccount::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Set a click listener for the terms and conditions text
        val termsAndConditionsTextView: TextView = findViewById(R.id.termsAndConditions)
        termsAndConditionsTextView.setOnClickListener {
            showTermsAndConditionsDialog()
        }
    }

    // Function to update the enabled state of the button based on checkbox states
    private fun updateButtonEnabledState() {
        button.isEnabled = checkbox1.isChecked && checkbox2.isChecked
        if (button.isEnabled) {
            // Change button background color when enabled
            button.setBackgroundResource(R.drawable.gradient_bg)
        } else {
            // Change button background color when disabled
            button.setBackgroundResource(R.color.disabled_btn)
        }
    }

    // Function to show the terms and conditions dialog box
    private fun showTermsAndConditionsDialog() {
        val builder = AlertDialog.Builder(this)

        // Inflate custom layout for the title view
        val titleView = layoutInflater.inflate(R.layout.dialog_title, null)
        builder.setCustomTitle(titleView)

        // Set the custom message view
        val messageView = layoutInflater.inflate(R.layout.dialog_message, null)
        builder.setView(messageView)

        // Set a positive button with custom text
        builder.setPositiveButton("Proceed") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Access and customize the title and message views
        val titleTextView: TextView = titleView.findViewById(R.id.titleTextView)
        titleTextView.text = "Custom Title"

        val messageTextView: TextView = messageView.findViewById(R.id.messageTextView)
        messageTextView.text = "Custom message goes here."

        dialog.show()
    }

}
