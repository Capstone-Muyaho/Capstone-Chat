package com.example.capstone_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_type.*

class ChooseTypeActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val db: DatabaseReference = Firebase.database.reference
        var id = "null"
        var email = "null"
        var ageRange = "null"
        var type: String
        var nickName: String

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                id = user.id.toString()    // 회원번호
                email = user.kakaoAccount?.email.toString()   // 카카오 계정
                ageRange = user.kakaoAccount?.ageRange.toString()  // 연령대
                Log.d("firebase", id)
            }
        }

        db.child("users").child(id).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

        btn_grandma.setOnClickListener {
            type = "P"
            // Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()
            btnNickName.setOnClickListener {
                nickName = edit_nickname.text.toString()
                // Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                writeNewUser(id, nickName, email, type, ageRange, db)

                val parentIntent = Intent(this, ParentMainActivity::class.java)
                parentIntent.putExtra("type", type)
                parentIntent.putExtra("nickName", nickName)
                startActivity(parentIntent)
            }
        }

        btn_son.setOnClickListener {
            type = "C"
            // Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()

            btnNickName.setOnClickListener {
                nickName = edit_nickname.text.toString()
                // Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                writeNewUser(id, nickName, email, type, ageRange, db)

                val childIntent = Intent(this, ChildMainActivity::class.java)
                childIntent.putExtra("type", type)
                childIntent.putExtra("nickName", nickName)
                startActivity(childIntent)
            }
        }
    }
//asdsad
    private fun writeNewUser(
        userId: String,
        nickname: String,
        email: String,
        type: String,
        ageRange: String,
        db: DatabaseReference
    ) {
        val user = User(userId, nickname, email, type, ageRange)
        db.child("users").child(userId).setValue(user)
    }
}