package com.example.mobile_auth.service

import com.auth0.android.jwt.JWT
import com.example.mobile_auth.NotificationHelper
import com.example.mobile_auth.Repository
import com.example.mobile_auth.utils.getToken
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FCMService : FirebaseMessagingService(), CoroutineScope by MainScope() {

    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"]
        val title = message.data["title"]
        val text = message.data["text"]

        val token = getToken(baseContext) ?: return

        val jwt = JWT(token)
        if (recipientId != jwt.getClaim("id").toString()) {
            NotificationHelper.simpleNotification(
                baseContext,
                requireNotNull(title),
                requireNotNull(text)
            )
        }
    }

    override fun onNewToken(token: String) {
        println(token)
        super.onNewToken(token)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            launch {
                println(it.token)
                val response = Repository.registerPushToken(it.token)
            }
        }
    }
}