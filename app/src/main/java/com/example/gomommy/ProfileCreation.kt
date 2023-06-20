package com.example.gomommy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import android.widget.TextView

class ProfileCreation: AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private var progressStatus = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation)

        progressBar = findViewById(R.id.progressBar)
        percentageText = findViewById(R.id.percentageText)

        // Start the progress animation
        startProgressAnimation()
    }

    private fun startProgressAnimation() {
        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus++
                handler.post {
                    progressBar.progress = progressStatus
                    percentageText.text = "$progressStatus%"
                }
                try {
                    // Delay to simulate progress update
                    Thread.sleep(60)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }
}
