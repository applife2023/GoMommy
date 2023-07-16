package com.example.gomommy

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gomommy.databinding.ActivityUserDueDateBinding
import com.example.gomommy.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class ProfileFragment : Fragment() {
    private lateinit var  binding: FragmentProfileBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")
        readUserProfile()
    }

    private fun readUserProfile(){
        dbRef.child("userProfile").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val value = it.value
            mapUserProfile(value)

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
    private fun mapUserProfile(userValue: Any?) {
        val userValue = userValue as? Map<String, Any?>
        if (userValue != null) {
            displayUserProfile(userValue["userName"],userValue["birthYear"],userValue["dueDate"])
        }
    }

    private fun displayUserProfile(userName: Any?,birthYear: Any?, dueDate: Any?) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val birthYear = birthYear.toString().toInt()
        val calculatedUserAge = currentYear - birthYear

        val userUsername = "Username: $userName"
        val userAge = "Age: $calculatedUserAge years old"
        val userDueDate = "Estimated due date: \n$dueDate"

        binding.userUsername.text = userUsername
        binding.userAge.text = userAge
        binding.userDueDate.text = userDueDate
    }
}