package com.example.gomommy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gomommy.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")

        readTimeStamp()

        return binding.root
    }

    private fun readDueDate() {
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener { snapshot ->
            val dueDate = snapshot.value as? String
            displayRemainingDate(dueDate)
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data", exception)
        }
    }

    private fun getCurrentDate(): Long {
        val currentDate = Calendar.getInstance().time
        return currentDate.time
    }

    private fun displayRemainingDate(dueDate: String?) {
        dueDate?.let {
            val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
            val parseDueDate = dateFormat.parse(dueDate)
            if (parseDueDate != null) {
                val remainingDays = calculateRemainingDays(parseDueDate)
                val remainingDaysString = remainingDays.toString()
                val remainDaysTextView = "Remaining days: $remainingDaysString"
                binding.remainingDaysTextView.text = remainDaysTextView
            } else {
                println("Invalid due date format.")
            }
        } ?: println("Due date not found.")
    }

    private fun calculateRemainingDays(dueDate: Date): Long {
        val currentTime = getCurrentDate()
        val dueTime = dueDate.time
        val remainingTime = dueTime - currentTime
        return remainingTime / (1000 * 60 * 60 * 24)
    }

    private fun displayDayN(timeStamp: String?) {
        val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
        val parseTimeStamp = dateFormat.parse(timeStamp)

        val dayN = calculateDayN(parseTimeStamp).toString()
        val dayNString = "Day $dayN"
        binding.dayNumberTextView.text = dayNString
    }

    private fun calculateDayN(timeStamp: Date): Long {
        val currentTime = getCurrentDate()
        val timeStampDate = timeStamp.time
        val dayNTime = currentTime - timeStampDate + (1000 * 60 * 60 * 24)
        return dayNTime / (1000 * 60 * 60 * 24)
    }


    private fun readTimeStamp() {
        dbRef.child("userProfile").child("firstDayTimeStamp").get().addOnSuccessListener { snapshot ->
            val timeStamp = snapshot.value as? String
            if (timeStamp != null) {
                timeStampChecker(true, timeStamp)
                readDueDate()
                displayDayN(timeStamp)
            } else {
                timeStampChecker(false, null)
            }
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data", exception)
        }
    }

    private fun timeStampChecker(value: Boolean, timeStamp: String?) {
        if (value) {
            println("timestamp already exists: $timeStamp")
        } else {
            println("timestamp added")
            saveTimeStamp()
        }
    }

    private fun saveTimeStamp() {
        val currentDate = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date())
        val newKeyValuePair = HashMap<String, Any>()
        newKeyValuePair["firstDayTimeStamp"] = currentDate
        dbRef.child("userProfile").updateChildren(newKeyValuePair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the image resource for babyGrowthImageView
        binding.babyGrowthImageView.setImageResource(R.drawable.img_first_2_weeks)

        // Update the text for babyGrowthTextView and momHealthTextView
        binding.babyGrowthTextView.text = "Baby's growth: Baby is but a glimmer in your eye. You're not pregnant yet at this stage of your journey. In fact, you probably have your period; the symptoms you’re experiencing are a result of PMS, not pregnancy."
        binding.momHealthTextView.text = "Mommy's changes: You can expect to experience your typical menstrual symptoms including bleeding, cramping, sore breasts, mood swings, etc."
    }
}