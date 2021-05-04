package com.example.capstone_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_type.*

class ChooseTypeActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val db: DatabaseReference = Firebase.database.reference
        var type: String
        var nickName: String

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val id = user.id.toString()    // 회원번호
                val email = user.kakaoAccount?.email.toString()   // 카카오 계정
                val ageRange = user.kakaoAccount?.ageRange.toString()  // 연령대


                btn_nickname.setOnClickListener {
                    when (type_group.checkedRadioButtonId) {
                        R.id.btn_grandpa -> tv.text = "부모로 설정\n"
                        R.id.btn_daughter -> tv.text = "자녀로 설정\n"
                    }
                }

                type_group.setOnCheckedChangeListener(CheckboxListener())
                type_group.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.btn_grandpa -> tv.text = "'부모'를 선택하셨습니다."
                        R.id.btn_daughter -> tv.text = "'자녀'를 선택하셨습니다."
                    }
                }

                btn_grandpa.setOnClickListener {
                    type = "P"
                    btn_nickname.setOnClickListener {
                        nickName = edit_nickname.text.toString()

                        writeNewUser(id, nickName, email, type, ageRange, db)

                        val parentIntent = Intent(this, ParentMainActivity::class.java)
                        parentIntent.putExtra("type", type)
                        parentIntent.putExtra("nickName", nickName)
                        startActivity(parentIntent)
                    }
                }

                btn_daughter.setOnClickListener {
                    type = "C"

                    btn_nickname.setOnClickListener {
                        nickName = edit_nickname.text.toString()

                        writeNewUser(id, nickName, email, type, ageRange, db)

                        val childIntent = Intent(this, ChildMainActivity::class.java)
                        childIntent.putExtra("type", type)
                        childIntent.putExtra("nickName", nickName)
                        startActivity(childIntent)
                    }
                }
            }
        }
    }

    inner class CheckboxListener: RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when(group?.id) {
                R.id.type_group ->
                    when(checkedId) {
                        R.id.btn_grandpa -> tv.text = "부모 설정"
                        R.id.btn_daughter -> tv.text = "자녀 설정"
                    }
            }
        }
    }

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