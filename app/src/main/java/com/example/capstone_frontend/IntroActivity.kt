package com.example.capstone_frontend

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}