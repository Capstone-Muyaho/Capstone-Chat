package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parent_main.*

class ParentMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_main)

        val type = intent.getStringExtra("type")
        val nickName = intent.getStringExtra("nickName")

        btnMyProfile.setOnClickListener {
            val intent = Intent(this, MyProfileActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("nickName", nickName)
            startActivity(intent)
        }
    }
}