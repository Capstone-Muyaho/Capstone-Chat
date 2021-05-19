package com.example.capstone_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        val friendList = arrayListOf(
            FriendInformation(R.drawable.user, "닉네임나와라", "타입나와라")
        )

        rv_friend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_friend.setHasFixedSize((true))
        rv_friend.adapter = FriendListAdapter(friendList)
    }

}
