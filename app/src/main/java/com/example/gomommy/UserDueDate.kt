package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gomommy.databinding.ActivityUserDueDateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserDueDate : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityUserDueDateBinding
    private lateinit var dayPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker
    private lateinit var btnSelectDate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDueDateBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")
        setContentView(binding.root)

        dayPicker = findViewById(R.id.dayPicker)
        monthPicker = findViewById(R.id.monthPicker)
        yearPicker = findViewById(R.id.yearPicker)
        btnSelectDate = findViewById(R.id.btnSelectDate)

        with(binding) {
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

                saveDueDate(dueDate)
                readDueDate()
                val intent = Intent(this@UserDueDate, ProfileCreation::class.java)
                intent.putExtra("due_date", dueDate)
                startActivity(intent)

                Toast.makeText(this@UserDueDate, "Selected Due Date: $dueDate", Toast.LENGTH_SHORT).show()
                // Add your desired logic for handling the selected due date here
            }
        }
    }

    private fun getCurrentYear(): Int {
        val calendar = java.util.Calendar.getInstance()
        return calendar.get(java.util.Calendar.YEAR)
    }

    private fun readDueDate(){
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
    private fun saveDueDate(dueDate: String) {

        val newKeyValuePair = HashMap<String, Any>()
        newKeyValuePair["dueDate"] = dueDate

        dbRef.child("userProfile").updateChildren(newKeyValuePair)
            .addOnCompleteListener{ Toast.makeText(this,"data stored sucessfully", Toast.LENGTH_LONG).show()
            }
    }
}
