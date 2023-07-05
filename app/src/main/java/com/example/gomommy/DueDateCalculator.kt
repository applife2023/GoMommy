package com.example.gomommy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DueDateCalculator: AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var calendarView: CalendarView
    private lateinit var btnSelectDate: Button

    private var selectedDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")
        setContentView(R.layout.activity_due_date_calculator)

        // Initialize views
        calendarView = findViewById(R.id.calendarView)
        btnSelectDate = findViewById(R.id.btnSelectDate)

        // Set initial button state
        btnSelectDate.isEnabled = false

        // Set listeners
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Convert selected date to milliseconds
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis

            // Enable button
            btnSelectDate.isEnabled = true
            btnSelectDate.setBackgroundResource(R.drawable.gradient_bg)
        }

        btnSelectDate.setOnClickListener {
            // Calculate due date based on the selected date
            val dueDate = calculateDueDate(selectedDate)
            // Display the due date in a toast message
            val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
            val formattedDueDate = dateFormat.format(dueDate)
            val message = "Estimated due date: $formattedDueDate"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            saveDueDate(formattedDueDate)
            readDueDate()
            val intent = Intent(this, ProfileCreation::class.java)
            intent.putExtra("due_date", dueDate)
            startActivity(intent)
            finish()
        }
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

    private fun calculateDueDate(selectedDate: Long): Date {
        // Perform your due date calculation based on the selectedDate
        // Replace this with your actual calculation logic
        val daysToAdd = 280 // Assuming a pregnancy duration of 280 days
        val dueDateMillis = selectedDate + daysToAdd * 24 * 60 * 60 * 1000 // Add days in milliseconds
        return Date(dueDateMillis)
    }


}
