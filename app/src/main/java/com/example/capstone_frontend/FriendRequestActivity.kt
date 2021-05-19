package com.example.capstone_frontend

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.activity_friend_request.*

class FriendRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request)

        val db: DatabaseReference = Firebase.database.getReference("users")
        var friendSearchList = ArrayList<User>()
        var friendSearch: User? = null
        var index = 0

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val id = user.id.toString()

                db.child(id).child("nickname").get().addOnSuccessListener {
                    val myName = it.value.toString()
                    db.child(id).child("friend").get().addOnSuccessListener {
                        val myFriendValue = it.value.toString()

                        db.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                friendSearchList.clear()

                                for (i: DataSnapshot in snapshot.children) {
                                    friendSearch = i.getValue(User::class.java)
                                    friendSearchList.add(friendSearch!!)

                                    if (myName == friendSearchList[index].friend.toString()) {
                                        val friendName = friendSearchList[index].nickname.toString()
                                        tv_friend_nickname.text = friendName
                                        tv_phrase1.text = "님에게서"
                                        tv_phrase2.text = "친구요청이 왔습니다."

                                        btn_accept.setOnClickListener {
                                            db.child(id).child("friend").setValue(friendName)
                                            Toast.makeText(this@FriendRequestActivity, "친구 요청을 수락하였습니다.", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@FriendRequestActivity, FriendListActivity::class.java)
                                            startActivity(intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP))
                                        }

                                        btn_reject.setOnClickListener {
                                            db.child(id).child("friend").setValue(null)
                                            db.child(friendSearchList[index].userid.toString()).child("friend").setValue(null)
                                            Toast.makeText(this@FriendRequestActivity, "친구 요청을 거절하였습니다.", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@FriendRequestActivity, FriendListActivity::class.java)
                                            startActivity(intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP))
                                        }
                                        break
                                    }
                                    index++
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@FriendRequestActivity, "Friends 읽어오기 실패.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }
    }
}