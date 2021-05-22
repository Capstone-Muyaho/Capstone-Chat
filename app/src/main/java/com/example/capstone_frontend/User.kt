package com.example.capstone_frontend

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val userid: String? = null,
                val nickname: String? = null,
                val kakaoemail: String? = null,
                val type: String? = null,
                val agerange: String? = null,
                val friend: String? = null,
                val chatroom: String? = null) {
}
