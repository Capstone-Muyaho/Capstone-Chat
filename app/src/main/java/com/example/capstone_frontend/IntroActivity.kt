package com.example.capstone_frontend

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val handler = Handler()
        val db: DatabaseReference = Firebase.database.getReference("users")

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("Token", "토큰 정보 보기 실패")

                handler.postDelayed({
                    val intent = Intent(applicationContext, LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000)
            } else if (tokenInfo != null) {
                Log.d("Token", "토큰 정보 보기 성공")

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

                                if (type == "P" || type == "C" && nickName != null) {
                                    val homeIntent = Intent(this, MainHomeActivity::class.java)
                                    homeIntent.putExtra("type", type)
                                    homeIntent.putExtra("nickName", nickName)
                                    startActivity(homeIntent)
                                } else {
                                    handler.postDelayed({
                                        val intent = Intent(applicationContext, LogInActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }, 2000)
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
    }
}