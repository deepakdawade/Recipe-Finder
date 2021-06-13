package com.devdd.recipe.firebase.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.devdd.recipe.data.utils.toDataClass
import com.devdd.recipe.data.utils.toJsonString
import com.devdd.recipe.firebase.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import kotlin.random.Random

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
        Timber.d("OnMessageReceived:${remoteMessage.toJsonString()}")
        Timber.d("OnMessageReceived MessageData ${remoteMessage.data}")

        val payload: NotificationPayload? = remoteMessage.toNotificationPayload()

        Timber.d("OnMessageReceived NotificationPayload: ${payload.toJsonString()}")
        Timber.d("OnMessageReceived Notification Body: ${remoteMessage.notification?.body}")
        payload?.let { sendNotification(it) }
    }

    private fun sendNotification(payload: NotificationPayload) {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        //Setting up Notification channels for android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = setupNotificationChannels()
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = Random.nextInt()

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(payload.title)
                .setContentText(payload.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

        notificationManager.notify(
            notificationId,
            notificationBuilder.build()
        )

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels(): NotificationChannel {
        val adminChannelName = getString(R.string.notification_channel_name)
        val adminChannelDescription = getString(R.string.notifications_channel_description)

        val notificationChannel = NotificationChannel(
            getString(R.string.notification_channel_id),
            adminChannelName,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = adminChannelDescription
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        return notificationChannel
    }
}

data class NotificationPayload(
    @SerializedName(value = "title", alternate = ["gcm_title"])
    @Expose
    val title: String?,
    @SerializedName(value = "message", alternate = ["gcm_alert"])
    @Expose
    val message: String?
)

fun RemoteMessage.toNotificationPayload(): NotificationPayload? {
    return try {
        val remoteMessageTree: JsonElement = Gson().toJsonTree(data)
        return remoteMessageTree.toJsonString().toDataClass()
    } catch (e: Exception) {
        null
    }
}
