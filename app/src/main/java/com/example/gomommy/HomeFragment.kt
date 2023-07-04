package com.example.gomommy

import android.icu.text.SimpleDateFormat
import android.icu.util.ULocale
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gomommy.databinding.ActivityUserDueDateBinding
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


        val currentDate = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(Date())
        binding.dayNumberTextView.text = currentDate

        readDueDate()

        return binding.root
    }

    private fun readDueDate() {
        dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val dueDate = it.value as? String
            displayRemaingDate(dueDate)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun displayRemaingDate(dueDate: String?) {
        dueDate?.let {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val currentDate = Calendar.getInstance().time
            val parseDueDate = dateFormat.parse(dueDate)

            if (parseDueDate!=null){
                val remainingDays = calculateRemainingDays(currentDate, parseDueDate)
                println("Remaining days: $remainingDays")
            }else {
                println("Invalid due date format.")
            }

        }?: println("Due date not found.")
    }

    private fun calculateRemainingDays(currentdate:Date, dueDate:Date): Long{
        val currentTime = currentdate.time
        val dueTime = dueDate.time
        val remainingTime = dueTime - currentTime
        return remainingTime / (1000 * 60 * 60 * 24)

    }

}