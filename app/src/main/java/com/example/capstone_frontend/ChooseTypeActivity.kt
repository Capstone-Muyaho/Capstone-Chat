package com.example.capstone_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChooseTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val button1 = findViewById<ImageButton>(R.id.btnGrandma)
        val button2 = findViewById<ImageButton>(R.id.btnSon)
        button1.setOnClickListener {
            val intent = Intent(this@ChooseTypeActivity, LogoutActivity::class.java) //부모 메인화면으로 가게 해야함
            Toast.makeText(applicationContext, "버튼 클릭 성공(부모)", Toast.LENGTH_SHORT).show()
            //버튼 클릭시 Toast 메세지"버튼 클릭 성공" 출력
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this@ChooseTypeActivity,LogoutActivity::class.java) //자식 메인화면으로 가게 해야함
            Toast.makeText(applicationContext, "버튼 클릭 성공(자녀)", Toast.LENGTH_SHORT).show()
            //버튼 클릭시 Toast 메세지"버튼 클릭 성공" 출력
            startActivity(intent)
        }
    }
}