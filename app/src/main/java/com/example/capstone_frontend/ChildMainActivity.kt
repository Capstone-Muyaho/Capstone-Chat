package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_parent_main.*

class ChildMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_main)

        val type = intent.getStringExtra("type")
        val nickName = intent.getStringExtra("nickName")

        btn_profile.setOnClickListener {
            val profileIntent = Intent(this, MyProfileActivity::class.java)
            profileIntent.putExtra("type", type)
            profileIntent.putExtra("nickName", nickName)
            startActivity(profileIntent)
        }

        btn_snaptalk.setOnClickListener {
            val snapTalkIntent = Intent(this, SnaptalkActivity::class.java)
            startActivity(snapTalkIntent)
        }

        /*Push messages*/
        val test: MyFirebaseInstanceIDService = MyFirebaseInstanceIDService()
        test.onTokenRefresh()
        Log.d(
            "FCM Message TEST",
            "REFRESHED TOKEN IS RIGHT ABOVE !!!"
        )
    }
}