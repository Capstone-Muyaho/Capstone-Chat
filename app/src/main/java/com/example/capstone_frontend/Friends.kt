package com.example.capstone_frontend

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friends(val user1: String? = null,
                val user2: String? = null,
                val permission: String? = null,
                val chatroom: String? = null) {
}
