package com.example.capstone_frontend

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.activity_type.*

class AddFriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        val db: DatabaseReference = Firebase.database.reference
        var userSearchList = ArrayList<User>()
        var friendsSearchList = ArrayList<Friends>()
        var friendSearch: Friends? = null
        var userSearch: User? = null
        var index = 0
        var isNickCheck = false

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val id = user.id.toString()

                db.child("users").child(id).child("nickname").get().addOnSuccessListener {
                    val myName = it.value.toString()

                    btn_add_friend.setOnClickListener() {
                        val friendName = edit_add_friend.text.toString()
                        val chatroomName = friendName + "-" + myName

                        if (friendName == null) {
                            Toast.makeText(this, "친구의 별명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            db.child("friends").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    friendsSearchList.clear()
                                    index = 0

                                    for (i: DataSnapshot in snapshot.children) {
                                        friendSearch = i.getValue(Friends::class.java)
                                        friendsSearchList.add(friendSearch!!)

                                        if (myName == friendsSearchList[index].user1.toString() && friendName == friendsSearchList[index].user2.toString()) { // 이미 친구 신청을 한 경우
                                            Toast.makeText(this@AddFriendsActivity, "이미 친구신청을 했습니다.", Toast.LENGTH_SHORT).show()
                                            break
                                        }

                                        if (friendName == friendsSearchList[index].user1.toString() && myName == friendsSearchList[index].user2.toString()) { // 친구 신청이 이미 와 있는 경우
                                            db.child("friends").child(chatroomName).child("permission").setValue("accept")
                                            Toast.makeText(this@AddFriendsActivity, "친구 신청이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                                            edit_add_friend.getText().clear()
                                            break
                                        }
                                        index++
                                    }

                                    if (!isNickCheck) {
                                        Toast.makeText(this@AddFriendsActivity, "존재하지 않는 별명입니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@AddFriendsActivity, "Friends 읽어오기 실패.", Toast.LENGTH_SHORT).show()
                                }
                            })

                            db.child("friends").child(chatroomName).child("permission").get().addOnSuccessListener { //
                                val check = it.value.toString()

                                if (check == "null") {
                                    db.child("users").addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            userSearchList.clear()
                                            index = 0

                                            for (i: DataSnapshot in snapshot.children) {
                                                userSearch = i.getValue(User::class.java)
                                                userSearchList.add(userSearch!!)

                                                if (friendName == userSearchList[index].nickname.toString()) {
                                                    writeNewFriend(myName, friendName, "reject", db)
                                                    Toast.makeText(this@AddFriendsActivity, "친구 신청이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                                                    edit_add_friend.getText().clear()
                                                    break
                                                }
                                                index++
                                            }

                                            if (!isNickCheck) {
                                                Toast.makeText(this@AddFriendsActivity, "존재하지 않는 별명입니다.", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(this@AddFriendsActivity, "Users 읽어오기 실패.", Toast.LENGTH_SHORT).show()
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

    private fun writeNewFriend(
        user1: String,
        user2: String,
        permission: String,
        db: DatabaseReference
    ) {
        val friend = Friends(user1, user2, permission)
        val chatroom = user1 + "-" + user2
        db.child("friends").child(chatroom).setValue(friend)
    }
}