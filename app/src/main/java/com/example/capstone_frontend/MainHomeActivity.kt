package com.example.capstone_frontend

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main_home.*

class MainHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)

        setPermission()

        val type = intent.getStringExtra("type")
        val nickName = intent.getStringExtra("nickName")

        btn_profile.setOnClickListener {
            val profileIntent = Intent(this, MyProfileActivity::class.java)
            profileIntent.putExtra("type", type)
            profileIntent.putExtra("nickName", nickName)
            startActivity(profileIntent)
        }

        btn_friendlist.setOnClickListener {
            val friendListIntent = Intent(this, FriendListActivity::class.java)
            startActivity(friendListIntent)
        }

        btn_snaptalk.setOnClickListener {
            val snapTalkIntent = Intent(this, SnaptalkActivity::class.java)
            startActivity(snapTalkIntent)
        }

        btn_emergency_call.setOnClickListener {
            try {
                var callIntent = Intent(Intent.ACTION_CALL)
                callIntent.setData(Uri.parse("tel:119"))
                startActivity(callIntent)
            } catch (e: Exception) {
                Log.d("call", "전화 실패")   
            }
        }

        /*Push messages
        val test: MyFirebaseInstanceIDService = MyFirebaseInstanceIDService()
        test.onTokenRefresh()
        Log.d(
            "FCM Message TEST",
            "REFRESHED TOKEN IS RIGHT ABOVE !!!"
        )*/
    }

    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                // Toast.makeText(this@MainHomeActivity, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부한 경우 수행
                Toast.makeText(this@MainHomeActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("긴급 전화를 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(
                android.Manifest.permission.CALL_PHONE
            )
            .check()
    }
}