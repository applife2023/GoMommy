package com.example.gomommy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private val shimmerDuration = 2000L // Duration of shimmer animation in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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
            // Handle the failure case
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

                // Show the content views when the data is ready
                showContent()
            } else {
                // Handle the case of an invalid due date format
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
            // Handle the failure case
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

    private fun startShimmerAnimation() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.tag = System.currentTimeMillis()
    }

    private fun stopShimmerAnimation() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun showContent() {
        binding.contentLayout.visibility = View.VISIBLE
    }

    private fun hideContent() {
        binding.contentLayout.visibility = View.INVISIBLE
    }

    private fun simulateDataLoading() {
        val shimmerStartTime = binding.shimmerLayout.tag as? Long ?: 0L
        val elapsedTime = System.currentTimeMillis() - shimmerStartTime
        val remainingTime = shimmerDuration - elapsedTime

        // Delay the display of content by 0.005 seconds (500 milliseconds)
        val delayDuration = 1000L

        if (remainingTime <= delayDuration) {
            // If the remaining time is less than the delay duration,
            // immediately stop shimmer animation and show content
            stopShimmerAnimation()
            // Add your actual data and update the UI here
            // For example:
            binding.dayNumberTextView.text = "Day 1"
            showContent()
        } else {
            // Simulate data loading delay
            binding.root.postDelayed({
                stopShimmerAnimation()
                // Add your actual data and update the UI here
                // For example:
                binding.dayNumberTextView.text = "Day 1"
                showContent()
            }, delayDuration)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the image resource for babyGrowthImageView
        binding.babyGrowthImageView.setImageResource(R.drawable.img_first_2_weeks)

        // Update the text for babyGrowthTextView and momHealthTextView
        binding.babyGrowthTextView.text =
            "Baby's growth: Baby is but a glimmer in your eye. You're not pregnant yet at this stage of your journey. In fact, you probably have your period; the symptoms you’re experiencing are a result of PMS, not pregnancy."
        binding.momHealthTextView.text =
            "Mommy's changes: You can expect to experience your typical menstrual symptoms including bleeding, cramping, sore breasts, mood swings, etc."

        // Hide the content views initially
        hideContent()

        // Start shimmer animation
        startShimmerAnimation()

        // Simulate data loading delay and stop shimmer animation
        simulateDataLoading()
    }
}