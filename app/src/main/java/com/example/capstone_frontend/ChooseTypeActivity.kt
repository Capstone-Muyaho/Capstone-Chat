package com.example.capstone_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk.type

class ChooseTypeActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val button1 = findViewById<RadioButton>(R.id.btnGrandma)
        val button2 = findViewById<RadioButton>(R.id.btnSon)
        val button3 = findViewById<Button>(R.id.btnNick)
        val edittext = findViewById<EditText>(R.id.edit_nickN)

        button1.setOnClickListener {
            val Stype = "P"
            Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()
            //버튼 클릭시 Toast 메세지 type 출력
            button3.setOnClickListener {
                val inputNick = edittext.text.toString()
                Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LogoutActivity::class.java)
                intent.putExtra("type", Stype)
                intent.putExtra("nickname", inputNick)
                startActivity(intent)
            }
        }
        button2.setOnClickListener {
            val Stype = "C"
            Toast.makeText(applicationContext, Stype, Toast.LENGTH_SHORT).show()

            button3.setOnClickListener {
                val inputNick = edittext.text.toString()
                Toast.makeText(this@ChooseTypeActivity, inputNick, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LogoutActivity::class.java)
                intent.putExtra("type", Stype)
                intent.putExtra("nickname", inputNick)
                startActivity(intent)
            }
        }
    }
}
