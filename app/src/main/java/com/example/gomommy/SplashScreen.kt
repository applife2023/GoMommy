package com.example.gomommy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private val splashDuration: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delay using Handler
        Handler().postDelayed(
            {
                // Start the main activity
                startActivity(Intent(this, WelcomeUser::class.java))

                // Close the splash activity
                finish()
            },
            splashDuration
        )
    }
}