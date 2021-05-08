package com.example.capstone_frontend

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_type.*

class ChooseTypeActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        var type: String
        var nickName: String

        var userSearchList = ArrayList<User>()
        var nickSearch: User? = null
        var position = 0
        var isUserCheck = false

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val id = user.id.toString()
                val email = user.kakaoAccount?.email.toString()
                val ageRange = user.kakaoAccount?.ageRange.toString()

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

                        if (nickName.equals("")) {
                            Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                            val userReference: DatabaseReference = database.getReference("users")

                            userReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    userSearchList.clear()
                                    position = 0
                                    isUserCheck = false

                                    for (i: DataSnapshot in snapshot.children) {
                                        nickSearch = i.getValue(User::class.java)
                                        userSearchList.add(nickSearch!!)

                                        Log.d("로그", userSearchList[position].nickname.toString())

                                        if (nickName == userSearchList[position].nickname.toString() && id != userSearchList[position].userid.toString()) {
                                            Toast.makeText(this@ChooseTypeActivity, "이미 존재하는 닉네임 입니다.", Toast.LENGTH_SHORT).show()
                                            isUserCheck = true
                                            break
                                        }
                                        position++
                                    }
                                    if (!isUserCheck) {
                                        writeNewUser(id, nickName, email, type, ageRange, userReference)

                                        val childIntent = Intent(this@ChooseTypeActivity,ChildMainActivity::class.java)
                                        childIntent.putExtra("type", type)
                                        childIntent.putExtra("nickName", nickName)
                                        startActivity(childIntent)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@ChooseTypeActivity, "읽어오기 실패.", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }

                btn_daughter.setOnClickListener {
                    type = "C"

                    btn_nickname.setOnClickListener {
                        nickName = edit_nickname.text.toString()

                        if (nickName.equals("")) {
                            Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                            val userReference: DatabaseReference = database.getReference("users")

                            userReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    userSearchList.clear()
                                    position = 0
                                    isUserCheck = false

                                    for (i: DataSnapshot in snapshot.children) {
                                        nickSearch = i.getValue(User::class.java)
                                        userSearchList.add(nickSearch!!)

                                        Log.d("로그", userSearchList[position].nickname.toString())

                                        if (nickName == userSearchList[position].nickname.toString() && id != userSearchList[position].userid.toString()) {
                                            Toast.makeText(this@ChooseTypeActivity, "이미 존재하는 닉네임 입니다.", Toast.LENGTH_SHORT ).show()
                                            isUserCheck = true
                                            break
                                        }
                                        position++
                                    }
                                    if (!isUserCheck) {
                                        writeNewUser(id, nickName, email, type, ageRange, userReference)

                                        val childIntent = Intent(this@ChooseTypeActivity, ChildMainActivity::class.java)
                                        childIntent.putExtra("type", type)
                                        childIntent.putExtra("nickName", nickName)
                                        startActivity(childIntent)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@ChooseTypeActivity, "읽어오기 실패.", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    inner class CheckboxListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when (group?.id) {
                R.id.type_group ->
                    when (checkedId) {
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
        db.child(userId).setValue(user)
    }
}