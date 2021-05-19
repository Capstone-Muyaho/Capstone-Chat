package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
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

        btn_friend_request.setOnClickListener {
            val db: DatabaseReference = Firebase.database.getReference("users")

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("TAG", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    val id = user.id.toString()

                    db.child(id).child("friend").get().addOnSuccessListener {
                        val friendValue = it.value.toString()

                        if (friendValue == "Waiting") { // 친구 요청이 와 있는 경우
                            val requestIntent = Intent(this, FriendRequestActivity::class.java)
                            startActivity(requestIntent)
                        } else {
                            Toast.makeText(this, "요청된 친구가 존재하지 않습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
