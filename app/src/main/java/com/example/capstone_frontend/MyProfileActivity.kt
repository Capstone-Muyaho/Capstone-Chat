package com.example.capstone_frontend

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        var id = "null"

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                id = user.id.toString()
            }
        }

        var tvType = findViewById(R.id.tv_type) as TextView
        val tvNickName = findViewById(R.id.tv_nickname) as TextView

        val type = intent.getStringExtra("type")
        val nickName = intent.getStringExtra("nickName")

        when (type) {
            "P" -> tvType.setText("부모")
            "C" -> tvType.setText("자녀")
            else -> Log.d("type", "부모-자녀 타입 입력 오류")
        }

        tvNickName.text = "${nickName}"

        btn_intent_add_friend.setOnClickListener() {
            val friendIntent = Intent(this, AddFriendActivity::class.java)
            startActivity(friendIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        btn_kakao_logout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }

        btn_kakao_unlink.setOnClickListener {
            val db: DatabaseReference = Firebase.database.reference
            db.child("users").child(id).removeValue()

            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}