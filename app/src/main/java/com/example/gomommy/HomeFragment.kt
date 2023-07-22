package com.example.gomommy

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

    var babyAgeInDays: Long = 0
    var babyAgeInWeeks: Long = 0
    var babyAgeInMonths: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (isConnectedToInternet()) {
            firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser?.uid
            dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")

            readTimeStamp()
        } else {
            showNoInternetDialog()
        }

        return binding.root
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    private fun showNoInternetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_no_internet, null)
        val dialogTitleTextView = dialogView.findViewById<TextView>(R.id.dialogTitleTextView)
        val dialogMessageTextView = dialogView.findViewById<TextView>(R.id.dialogMessageTextView)
        val retryButton = dialogView.findViewById<Button>(R.id.retryButton)
        val exitButton = dialogView.findViewById<Button>(R.id.exitButton)

        dialogTitleTextView.text = "No Internet Connection"
        dialogMessageTextView.text = "Please check your internet connection and try again."

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Apply the custom button background programmatically
        retryButton.setBackgroundResource(R.drawable.gradient_bg)
        exitButton.setBackgroundResource(R.drawable.gradient_bg)

        // Customize other button attributes as needed
        retryButton.setTextColor(resources.getColor(R.color.white))
        exitButton.setTextColor(resources.getColor(R.color.white))

        // Set click listeners for the buttons
        retryButton.setOnClickListener {
            if (isConnectedToInternet()) {
                // Internet connection is available, perform the necessary actions
                firebaseAuth = FirebaseAuth.getInstance()
                val firebaseUser = firebaseAuth.currentUser?.uid
                dbRef = FirebaseDatabase.getInstance().getReference("Users/$firebaseUser")

                readTimeStamp()
            } else {
                // Internet connection is still not available, show the dialog again
                showNoInternetDialog()
            }
            dialog.dismiss()
        }

        exitButton.setOnClickListener {
            // Exit the activity or perform any other action
            activity?.finish()
            dialog.dismiss()
        }
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

    private fun calculateRemainingDays(dueDate: Date): Long {
        val currentTime = getCurrentDate()
        val dueTime = dueDate.time
        val remainingTime = dueTime - currentTime
        return remainingTime / (1000 * 60 * 60 * 24)
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

    private fun displayDayN(timeStamp: String?, dueDate: String?) {
        val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
        val parseTimeStamp = timeStamp?.let { dateFormat.parse(it) }
        val parseDueDate = dueDate?.let { dateFormat.parse(it) }

        if (parseTimeStamp != null && parseDueDate != null) {
            val dayN = calculateDayN(parseTimeStamp).toString()
            val remainingDays = calculateRemainingDays(parseDueDate)
            val differenceDays = 280 - remainingDays

            println(dayN)

            val (weeks, days) = calculateWeeksAndDays(dayN.toLong())

            println(weeks)

            var ageInWeeks = weeks + differenceDays / 7
            var ageInDays = days + differenceDays % 7


            if (ageInDays < 0) {
                ageInWeeks--
                ageInDays += 7
            }

            // Update public variables with the calculated values
            babyAgeInWeeks = ageInWeeks
            babyAgeInDays = ageInDays
            babyAgeInMonths = calculateMonths(ageInWeeks)

            displayBabyGrowthImage(differenceDays.toInt())

            val dayNString = "Week $ageInWeeks, Day $ageInDays"
            binding.dayNumberTextView.text = dayNString
        } else {
            // Handle the case of invalid date format
        }
    }

    // Calculate baby's age in months based on weeks
    private fun calculateMonths(weeks: Long): Long {
        return weeks / 4 // Assuming one month has 4 weeks
    }

    private fun calculateWeeksAndDays(dayN: Long): Pair<Long, Long> {
        val weeks = dayN / 7
        val remainingDays = dayN % 7
        return Pair(weeks, remainingDays)
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
                // Call readDueDate() first to get the dueDate value
                dbRef.child("userProfile").child("dueDate").get().addOnSuccessListener { snapshot ->
                    val dueDate = snapshot.value as? String
                    displayDayN(timeStamp, dueDate)
                }
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
            stopShimmerAnimation()
            showContent()
        } else {
            // Simulate data loading delay
            binding.root.postDelayed({
                stopShimmerAnimation()
                showContent()
            }, delayDuration)
        }
    }

    private fun displayBabyGrowthImage(dayN: Int) {
        println(dayN)
        val imageResourceId = when (dayN) {
            in 1..6 -> R.drawable.baby_growth_week_1n6days // Set the appropriate image resource for weeks 1 to 12
            in 7..14 -> R.drawable.baby_growth_week_2 // Set the appropriate image resource for weeks 13 to 24
            in 15..22 -> R.drawable.baby_growth_week_3 // Set the appropriate image resource for weeks 25 to 36
            in 23..200 -> R.drawable.baby_growth_week_20 // Set the appropriate image resource for weeks 37 to 280 (full-term)
            else -> R.drawable.baby_growth_week_40 // Set a default image resource for any invalid weeks value
        }


        binding.babyGrowthImageView.setImageResource(imageResourceId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the image resource for babyGrowthImageView
//        binding.babyGrowthImageView.setImageResource(R.drawable.weeks_1n2)

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