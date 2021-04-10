package com.example.capstone_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_type.*

class ChooseTypeActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        var id = -1L
        var email = "null"
        var ageRange = "null"

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                id = user.id    // 회원번호
                email = user.kakaoAccount?.email.toString()   // 카카오 계정
                ageRange = user.kakaoAccount?.ageRange.toString()  // 연령대
            }
        }
        
        /*
        DB에 사용자 정보(Stype, inputNick)가 입력되어있는 경우 바로 MainActivity로 넘어간다.
        토큰 정보 가져온 뒤 해당 값(회원번호 또는 카카오 이메일) 이용해서 Stype, nickname 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                id = user.idS
            }
        }

        if
            val intent = Intent(this, ParentMainActivity::class.java)
                intent.putExtra("type", Stype)
                intent.putExtra("nickname", inputNick)
                startActivity(intent)
        } else if (Stype == "C") {
            val intent = Intent(this, ChildMainActivity::class.java)
                intent.putExtra("type", Stype)
                intent.putExtra("nickname", inputNick)
                startActivity(intent)
        } else { 아래 전체 내용
         */

//        val editText = findViewById<EditText>(R.id.edit_nickN)
        var type: String
        var nickName: String

        btnGrandma.setOnClickListener {
            type = "P"
            // Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()
            btnNickName.setOnClickListener {
                nickName = editNickName.text.toString()
                // Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                writeNewUser(id, nickName, email, type, ageRange) // DB에 회원정보 저장

                val intent = Intent(this, ParentMainActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("nickName", nickName)
                startActivity(intent)
            }
        }

        btnSon.setOnClickListener {
            type = "C"
            // Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()

            btnNickName.setOnClickListener {
                nickName = editNickName.text.toString()
                // Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                writeNewUser(id, nickName, email, type, ageRange) // DB에 회원정보 저장

                val intent = Intent(this, ChildMainActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("nickName", nickName)
                startActivity(intent)
            }
        }
    }

    fun writeNewUser(userId: Long, nickname: String, email: String, type: String, ageRange: String) {
        val db: DatabaseReference
        db = Firebase.database.reference
        val user = User(userId, nickname, email, type, ageRange)

        db.child("users").child(nickname).setValue(user)
    }
}
