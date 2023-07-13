package com.example.gomommy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.gomommy.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashDuration: Long = 2000 // 2 seconds
    private val isFirstTimeKey = "isFirstTime"
    private val hasCreatedAccountKey = "hasCreatedAccount"
    private val isLoggedInKey = "isLoggedIn"
    private val isLoggedOutKey = "isLoggedOut"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Check if the app is launched for the first time or after reinstallation
        val isFirstTime = sharedPrefs.getBoolean(isFirstTimeKey, true)
        val hasCreatedAccount = sharedPrefs.getBoolean(hasCreatedAccountKey, false)

        // Delay using Handler to display the splash screen
        Handler().postDelayed(
            {
                if (isFirstTime) {
                    // Set the isFirstTime flag to false
                    val editor: SharedPreferences.Editor = sharedPrefs.edit()
                    editor.putBoolean(isFirstTimeKey, false)
                    editor.apply()

                    // Redirect to the introduction or onboarding activity
                    startActivity(Intent(this@SplashScreen, WelcomeUser::class.java))
                } else {
                    // Check if the user has created an account
                    if (hasCreatedAccount) {
                        // Redirect to the login activity
                        startActivity(Intent(this@SplashScreen, LoginAccount::class.java))
                    } else {
                        // Redirect to the account creation activity
                        startActivity(Intent(this@SplashScreen, LoginAccount::class.java))
                    }
                }

                // Close the splash activity
                finish()
            },
            splashDuration
        )
    }
}
