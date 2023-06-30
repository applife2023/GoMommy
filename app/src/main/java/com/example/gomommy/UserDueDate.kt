package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UserDueDate : AppCompatActivity() {
    private lateinit var btnSelectDate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_due_date)
        btnSelectDate = findViewById(R.id.btnSelectDate)



        btnSelectDate.setOnClickListener {
            // Get the selected year from the NumberPicker
            //val selectedYear = if (npYear.value == 0) null else yearRange[npYear.value].toInt()

            // Update the selected year in the TextView
            //tvSelectedYear.text = "Selected Year: ${selectedYear ?: "Not selected"}"
            val intent = Intent(this, ProfileCreation::class.java)
            startActivity(intent)
        }
    }
}