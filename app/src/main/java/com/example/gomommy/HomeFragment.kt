package com.example.gomommy

import android.icu.text.SimpleDateFormat
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



    private fun readDueDate(timeStamp: String?) {
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val dueDate = it.value as? String
            displayRemainingDate(dueDate)
            displayDayN(timeStamp)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun getCurrentDate(): Long {
        val currentDate = Calendar.getInstance().time
        return currentDate.time
    }

    private fun displayRemainingDate(dueDate: String?) {
        dueDate?.let {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val parseDueDate = dateFormat.parse(dueDate)
            if (parseDueDate!=null){
                val remainingDays = calculateRemainingDays(parseDueDate)
                val remainingDaysString = remainingDays.toString()
                val remainDaysTextView = ("Remaining days: $remainingDaysString")
                binding.remainingDaysTextView.text = remainDaysTextView
            }else {
                println("Invalid due date format.")
            }

        }?: println("Due date not found.")
    }

    private fun calculateRemainingDays(dueDate: Date): Long {
        val currentTime = getCurrentDate()
        val dueTime = dueDate.time
        val remainingTime = dueTime - currentTime
        return remainingTime / (1000 * 60 * 60 * 24)
    }

    private fun displayDayN(timeStamp: String?) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val parseTimeStamp = dateFormat.parse(timeStamp)

        val dayN = calculateDayN(parseTimeStamp).toString()
        val dayNString = "Day $dayN"
        binding.dayNumberTextView.text = dayNString
    }

    private fun calculateDayN(timeStamp: Date): Long {
        val currentTime = getCurrentDate()
        val timeStamp = timeStamp.time
        val dayNTime = currentTime - (timeStamp) + (1000 * 60 * 60 * 24)
        return dayNTime / (1000 * 60 * 60 * 24)
    }

    private fun readTimeStamp(){
        dbRef.child("userProfile").child("firstDayTimeStamp").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val timeStamp = it.value as? String
            if (it.value != null){
                timeStampChecker(true, timeStamp)
                readDueDate(timeStamp)
            }else{timeStampChecker(false, null)}
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)

        }
    }

    private fun timeStampChecker(value: Boolean, timeStamp: String?){
        if (value){
            println("timestamp already exist: $timeStamp")
        } else{
            println("timestamp added")
            saveTimeStamp()
        }
    }

    private fun saveTimeStamp(){
        val currentDate = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault()).format(Date())
        val newKeyValuePair = HashMap<String, Any>()
        newKeyValuePair["firstDayTimeStamp"] = currentDate
        dbRef.child("userProfile").updateChildren(newKeyValuePair)
    }
}