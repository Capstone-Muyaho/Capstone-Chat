package com.example.capstone_frontend

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(
                TAG,
                "FCM Data Message : " + remoteMessage.data
            )
        }
        if (remoteMessage.notification != null) {
            val messageBody = remoteMessage.notification!!.body
            Log.d(
                TAG,
                "FCM Notification Message Body : $messageBody"
            )
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(applicationContext, messageBody, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "MyFCMService"
    }
}