package com.example.capstone_frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // 이미 로그인이 되어있는 경우
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                // Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()

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
                val intent = Intent(this, ChooseTypeActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

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
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val chooseTypeIntent = Intent(this, ChooseTypeActivity::class.java)
                startActivity(chooseTypeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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