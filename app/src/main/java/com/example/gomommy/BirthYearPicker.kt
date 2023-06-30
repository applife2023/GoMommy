package com.example.gomommy

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import java.time.Year

class BirthYearPicker : AppCompatActivity() {
    private lateinit var btnSelectYear: Button
    //private lateinit var tvSelectedYear: TextView
    private lateinit var npYear: NumberPicker

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birth_year_picker)

        btnSelectYear = findViewById(R.id.btnSelectYear)
        btnSelectYear.isEnabled = false
        //tvSelectedYear = findViewById(R.id.tvSelectedYear)
        npYear = findViewById(R.id.npYear)

        val currentYear = Year.now().value
        println(currentYear)
        val startYear = 1980
        val endYear = currentYear - 13

        npYear.minValue = 0
        npYear.maxValue = endYear - startYear + 1
        npYear.displayedValues = buildDisplayValues(startYear, endYear)
        npYear.value = npYear.maxValue / 2 // Set initial value to the middle index
        npYear.wrapSelectorWheel = false
        npYear.textSize = 55f

        npYear.setOnScrollListener { _, _ ->
            // Enable the Next button when the NumberPicker is scrolled
            updateButtonEnabledState()
        }
        updateButtonEnabledState()

        btnSelectYear.setOnClickListener {
            // Get the selected year from the NumberPicker
            //val selectedYear = if (npYear.value == 0) null else yearRange[npYear.value].toInt()

            // Update the selected year in the TextView
            //tvSelectedYear.text = "Selected Year: ${selectedYear ?: "Not selected"}"
            val intent = Intent(this, UserDueDate::class.java)
            startActivity(intent)
        }
    }
    private fun buildDisplayValues(startYear: Int, endYear: Int): Array<String> {
        val yearRange = ArrayList<String>()
        val selectOption = "Select"
        val middleIndex = (endYear - startYear) / 2

        for (year in startYear..endYear) {
            yearRange.add(year.toString())
        }
        yearRange.add(middleIndex, selectOption)

        return yearRange.toTypedArray()
    }

    private fun updateButtonEnabledState() {
        val selectedValue = npYear.displayedValues[npYear.value]
        if (selectedValue == "Select") {
            // Disable the button and set the background color for disabled state
            btnSelectYear.isEnabled = false
            btnSelectYear.setBackgroundResource(R.color.disabled_btn)
        } else {
            // Enable the button and set the background color for enabled state
            btnSelectYear.isEnabled = true
            btnSelectYear.setBackgroundResource(R.drawable.gradient_bg)
        }
    }
}