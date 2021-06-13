package com.devdd.recipe.firebase.service

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.devdd.recipe.data.utils.toJsonString
import com.devdd.recipe.firebase.utils.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class RecipeFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Firebase New Token:$token")
        //sendNotificationTOServer()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val message = "OnMessageReceived:${remoteMessage.toJsonString()}"
        Timber.d(message)
        remoteMessage.data.let {
            Timber.d(TAG, "Message data payload: %s", remoteMessage.data)
        }
        remoteMessage.notification?.let {
            Timber.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body ?: "Body is null")
        }
    }

    private fun sendNotification(body: String) {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(body, applicationContext)
    }
}