package com.example.gomommy

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ScrollView
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
    @SuppressLint("SetTextI18n")
    private fun showTermsAndConditionsDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialogStyle)

        // Inflate custom layout for the title view
        val titleView = layoutInflater.inflate(R.layout.dialog_title, null)
        builder.setCustomTitle(titleView)

        // Set the custom message view
        val scrollView = ScrollView(this)
        val messageView = layoutInflater.inflate(R.layout.dialog_message, scrollView, false)
        scrollView.addView(messageView)
        builder.setView(scrollView)

        // Set a positive button with custom text
        builder.setPositiveButton("I Understand") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Access and customize the title and message views
        val titleTextView: TextView = titleView.findViewById(R.id.titleTextView)
        titleTextView.text = "User Terms and Conditions:"

        val messageTextView: TextView = messageView.findViewById(R.id.messageTextView)
        messageTextView.text =
            "Acceptance of Terms:\nBy downloading, accessing, or using the Go Mommy application, you agree to be bound by these User Terms and Conditions. If you do not agree to these Terms, please refrain from using the App.\n\n" +
                    "Age Requirement:\nYou must be at least 18 years old to use the App. By using the App, you represent and warrant that you are of legal age to enter into a binding agreement.\n\n" +
                    "Collection of Information \n" +
                    "a. User Data: When using the App, you may be required to provide certain information, including your age, Google account information, location, due date, and other sensitive data related to your pregnancy. You understand and acknowledge that providing accurate and up-to-date information is your responsibility.\n" +
                    "\nb. Privacy and Data Security: We value your privacy and are committed to protecting your personal information. Please refer to our Privacy Policy for detailed information on how we collect, use, and safeguard your data.\n\n" +
                    "Use of the App \na. License: Subject to your compliance with these Terms, we grant you a limited, non-exclusive, non-transferable, revocable license to use the App for personal, non-commercial purposes.\n" +
                    "\nb. Prohibited Conduct: You agree not to: \n" +
                    "i. Use the App for any illegal, unauthorized, or unethical purposes; \n" +
                    "ii. Violate any applicable laws, regulations, or third-party rights; \n" +
                    "iii. Interfere with the operation or security of the App; \n" +
                    "iv. Transmit any viruses, malware, or harmful code; \n" +
                    "v. Engage in any activity that may disrupt or impair the App's functionality or performance.\n\n" +
                    "Intellectual Property Rights \n" +
                    "a. User Content: By submitting any content through the App, you grant Go Mommy a non-exclusive, worldwide, royalty-free license to use, reproduce, modify, distribute, and display such content for the purpose of providing and promoting the App.\n\n" +
                    "Limitation of Liability \n" +
                    "a. Disclaimer: The App is provided \"as is\" without any warranties, expressed or implied. We do not guarantee the accuracy, completeness, or reliability of the App or its content.\n" +
                    "b. Limitation of Liability: Go Mommy and its affiliates shall not be liable for any direct, indirect, incidental, consequential, or punitive damages arising out of or in connection with the use or inability to use the App.\n\n" +
                    "Privacy Policy:\n" +
                    "Please refer to our separate Privacy Policy document for detailed information on how we collect, use, disclose, and protect your personal information.\n\n" +
                    "Terms of Use:\n" +
                    "Please refer to our separate Terms of Use document for detailed information on your rights and obligations when using the Go Mommy application.\n" +
                    "\nRemember to consult with a legal professional to ensure compliance with the specific laws and regulations applicable to your jurisdiction.\n" +
                    "\n\n"

        // Set fixed width and height for the dialog
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(dialog.window?.attributes)
            width = resources.getDimensionPixelSize(R.dimen.dialog_width)
            height = resources.getDimensionPixelSize(R.dimen.dialog_height)
        }
        dialog.show()
        dialog.window?.attributes = layoutParams
    }
}
