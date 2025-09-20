package com.example.animefind.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.animefind.MainActivity
import com.example.animefind.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Play short sound (page flip / chime)
        val mediaPlayer = MediaPlayer.create(this, R.raw.shine_sound)
        mediaPlayer.start()

        // Delay then go to MainActivity
        window.decorView.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500) // 1.5 sec
    }
}
