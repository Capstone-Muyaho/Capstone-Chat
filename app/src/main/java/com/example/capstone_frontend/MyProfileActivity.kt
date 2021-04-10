package com.example.capstone_frontend

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.w3c.dom.Text

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                kakaoEmail.text = "이메일: ${user?.kakaoAccount?.email}"
            }
        }

        var tvType = findViewById(R.id.tvType) as TextView
        val tvNickName = findViewById(R.id.tvNickName) as TextView

        val type = intent.getStringExtra("type")
        val nickName = intent.getStringExtra("nickName")

        if (type == "P") {
            tvType.setText("부모")
        } else if (type == "C") {
            tvType.setText("자녀")
        } else {
            Log.d("type", "부모-자녀 타입 오류")
        }

        tvNickName.text = "닉네임: ${nickName}"

        btnKakaoLogout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        btnKakaoUnlink.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, IntroActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }
    }
}