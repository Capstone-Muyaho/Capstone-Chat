package com.example.capstone_frontend

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro) //xml , java 소스 연결
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, LogInActivity::class.java)
            startActivity(intent) //다음화면으로 넘어감
            finish()
        }, 2000) //2초 뒤에 Runner객체 실행하도록 함
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}