package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val db: DatabaseReference = Firebase.database.getReference("users")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Token", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        val id = user.id.toString()
                        Log.d("Token", "사용자 정보 요청 성공")

                        db.child(id).child("type").get().addOnSuccessListener {
                            val type = it.value.toString()

                            db.child(id).child("nickname").get().addOnSuccessListener {
                                val nickName = it.value.toString()

                                if (type == "P" && nickName != null) {
                                    val parentIntent = Intent(this, ParentMainActivity::class.java)
                                    parentIntent.putExtra("type", type)
                                    parentIntent.putExtra("nickName", nickName)
                                    startActivity(parentIntent)
                                } else if (type == "C" && nickName != null) {
                                    val childIntent = Intent(this, ChildMainActivity::class.java)
                                    childIntent.putExtra("type", type)
                                    childIntent.putExtra("nickName", nickName)
                                    startActivity(childIntent)
                                } else {
                                    val intent = Intent(this, ChooseTypeActivity::class.java)
                                    startActivity(intent)
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }
                    }
                }
            }
        }

        btn_kakao_login.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}