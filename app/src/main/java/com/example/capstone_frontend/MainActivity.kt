package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var LogInIntent = Intent(this, LogInActivity::class.java)
        Handler().postDelayed({
            startActivity(LogInIntent)
        }, 2000)
        /* key 구하기
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        */
    }
}