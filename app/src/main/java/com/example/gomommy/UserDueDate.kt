package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserDueDate : AppCompatActivity() {

    private lateinit var dayPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker
    private lateinit var btnSelectDate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_due_date)

        dayPicker = findViewById(R.id.dayPicker)
        monthPicker = findViewById(R.id.monthPicker)
        yearPicker = findViewById(R.id.yearPicker)
        btnSelectDate = findViewById(R.id.btnSelectDate)

        // Set the minimum and maximum values for the pickers
        val currentYear = getCurrentYear()
        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        yearPicker.minValue = currentYear
        yearPicker.maxValue = currentYear + 1

        // Set a custom formatter for the month picker
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        monthPicker.displayedValues = monthNames

        // Set a scroll listener for the pickers
        val pickerScrollListener = NumberPicker.OnScrollListener { _, _ ->
            btnSelectDate.isEnabled = true
            btnSelectDate.setBackgroundResource(R.drawable.gradient_bg)
        }
        dayPicker.setOnScrollListener(pickerScrollListener)
        monthPicker.setOnScrollListener(pickerScrollListener)
        yearPicker.setOnScrollListener(pickerScrollListener)

        // Disable the button initially
        btnSelectDate.isEnabled = false
        btnSelectDate.setBackgroundResource(R.drawable.disabled_btn)

        // Set a click listener for the "Next" button
        btnSelectDate.setOnClickListener {
            val selectedDay = dayPicker.value
            val selectedMonth = monthPicker.value
            val selectedYear = yearPicker.value

            val dueDate = "$selectedDay ${monthNames[selectedMonth - 1]} $selectedYear"

            val intent = Intent(this, ProfileCreation::class.java)
            intent.putExtra("due_date", dueDate)
            startActivity(intent)

            Toast.makeText(this, "Selected Due Date: $dueDate", Toast.LENGTH_SHORT).show()
            // Add your desired logic for handling the selected due date here
        }
    }

    private fun getCurrentYear(): Int {
        val calendar = java.util.Calendar.getInstance()
        return calendar.get(java.util.Calendar.YEAR)
    }
}
