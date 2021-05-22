package com.example.capstone_frontend

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
import kotlinx.android.synthetic.main.activity_type.*

class AddFriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        val db: DatabaseReference = Firebase.database.getReference("users")
        var userSearchList = ArrayList<User>()
        var userSearch: User? = null
        var index = 0
        var isUserCheck = false

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val id = user.id.toString()

                db.child(id).child("nickname").get().addOnSuccessListener {
                    val myName = it.value.toString()
                    db.child(id).child("friend").get().addOnSuccessListener {
                        val myFriend = it.value.toString()
                        db.child(id).child("type").get().addOnSuccessListener {
                            val myType = it.value.toString()
                            db.child(id).child("chatroom").get().addOnSuccessListener {
                                val myChat = it.value.toString()

                                btn_add_friend.setOnClickListener() {
                                    val friendName = edit_add_friend.text.toString()

                                    if (friendName == "") {
                                        Toast.makeText(this, "친구의 별명을 입력해주세요.", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        db.addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                userSearchList.clear()
                                                index = 0

                                                for (i: DataSnapshot in snapshot.children) {
                                                    userSearch = i.getValue(User::class.java)
                                                    userSearchList.add(userSearch!!)

                                                    if (myChat != null || userSearchList[index].chatroom.toString() != null) {
                                                        Toast.makeText(this@AddFriendActivity, "올바르지 않은 요청입니다.", Toast.LENGTH_SHORT).show()
                                                        break
                                                    }

                                                        if (myName == userSearchList[index].friend.toString() && myFriend == "Waiting") { // 상대방이 이미 친구 신청을 한 경우
                                                            db.child(id).child("friend").setValue(friendName)
                                                            Toast.makeText(this@AddFriendActivity, "친구 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                                                            if (myType == "P") {
                                                                val chatRoom = myName + "-" + userSearchList[index].nickname
                                                                db.child(id).child("chatroom").setValue(chatRoom)
                                                                db.child(userSearchList[index].userid.toString()).child("chatroom").setValue(chatRoom)
                                                            } else { // myType == "C"
                                                                val chatRoom = userSearchList[index].nickname + "-" + myName
                                                                db.child(id).child("chatroom").setValue(chatRoom)
                                                                db.child(userSearchList[index].userid.toString()).child("chatroom").setValue(chatRoom)
                                                            }

                                                            edit_add_friend.getText().clear()
                                                            isUserCheck = true
                                                            break
                                                        } else if (friendName == userSearchList[index].nickname.toString()) {
                                                            db.child(userSearchList[index].userid.toString()).child("friend").setValue("Waiting")
                                                            db.child(id).child("friend").setValue(friendName)
                                                            Toast.makeText(this@AddFriendActivity, "친구 신청이 완료 되었습니다.", Toast.LENGTH_SHORT).show()

                                                            edit_add_friend.getText().clear()
                                                            isUserCheck = true
                                                            break
                                                        } else if (myFriend == userSearchList[index].nickname.toString() && myName == userSearchList[index].friend.toString()) {
                                                            Toast.makeText(this@AddFriendActivity, "이미 친구인 사람입니다.", Toast.LENGTH_SHORT).show()
                                                            break
                                                        }
                                                    index++
                                                }

                                                if (!isUserCheck) {
                                                    Toast.makeText(
                                                        this@AddFriendActivity,
                                                        "존재하지 않는 별명입니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(
                                                    this@AddFriendActivity,
                                                    "Friends 읽어오기 실패.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}