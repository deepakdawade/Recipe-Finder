package com.devdd.recipe.firebase.service

import com.devdd.recipe.data.utils.toJsonString
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class RecipeFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Firebase New Token:$token")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val message = "OnMessageReceived:${p0.toJsonString()}"
        Timber.d(message)
    }
}