package com.example.mobile_auth.service

import com.auth0.android.jwt.JWT
import com.example.mobile_auth.utils.getToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"]
        val title = message.data["title"]
        println(recipientId)
        println(title)

        val token = getToken(baseContext) ?: return

        val jwt = JWT(token)
        if (recipientId != jwt.getClaim("id").toString()) {
            // TODO: send request to server
            return

            // TODO() show notification
        }
    }

    override fun onNewToken(token: String) {
        println(token)

    }
}