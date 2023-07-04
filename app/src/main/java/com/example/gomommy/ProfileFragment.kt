package com.example.gomommy

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

class ProfileFragment : Fragment() {
    private lateinit var  binding: FragmentProfileBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")
        readUserProfile()
    }

    private fun readUserProfile(){
        dbRef.child("userProfile").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val value = it.value
            displayUserProfile(value)

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
    private fun displayUserProfile(userValue: Any?) {
        val userValue = userValue as? Map<String, Any?>
        if (userValue != null) {
            println(userValue["userName"])
        }
    }
}