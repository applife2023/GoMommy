package com.example.gomommy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.gomommy.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashDuration: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Delay using Handler to display the splash screen
        Handler().postDelayed(
            {
                // Check if the user is logged in or has created an account
                val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

                // Redirect based on login status
                if (isLoggedIn) {
                    startActivity(Intent(this@SplashScreen, Homepage::class.java))
                } else {
                    startActivity(Intent(this@SplashScreen, LoginAccount::class.java))
                }

                // Close the splash activity
                finish()
            },
            splashDuration
        )
    }
}
