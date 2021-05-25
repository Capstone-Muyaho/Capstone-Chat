package com.example.capstone_frontend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.capstone_frontend.databinding.ActivityMainBinding

class TmpActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val REQUEST_EXTERNAL_STORAGE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        // 저장소 읽기 권한이 없을 시 권한 요청 팝업 생성
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val readPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        }
        initUI()
    }

    private fun initUI() {
        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.enterBtn.setOnClickListener({ v ->
            val intent = Intent(applicationContext, ChatActivity::class.java)
            intent.putExtra("username", binding.usernameEdit.getText().toString())
            intent.putExtra("roomNumber", binding.roomEdit.getText().toString())
            startActivity(intent)
        })
    }
}