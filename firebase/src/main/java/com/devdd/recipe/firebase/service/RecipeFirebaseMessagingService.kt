package com.devdd.recipe.firebase.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.devdd.recipe.data.utils.toJsonString
import com.devdd.recipe.domain.executers.firebase.SendFcmTokenToServer
import com.devdd.recipe.firebase.R
import com.devdd.recipe.firebase.constant.NotificationPayload
import com.devdd.recipe.firebase.constant.toNotificationPayload
import com.devdd.recipe.firebase.parse.ParseNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class RecipeFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    @Inject
    lateinit var parseNotification: ParseNotification

    @Inject
    lateinit var sendFcmTokenToServer: SendFcmTokenToServer

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Firebase New Token:$token")
        sendNotificationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("OnMessageReceived:${remoteMessage.toJsonString()}")
        Timber.d("OnMessageReceived MessageData ${remoteMessage.data}")

        val payload: NotificationPayload = remoteMessage.toNotificationPayload()

        Timber.d("OnMessageReceived NotificationPayload: ${payload.toJsonString()}")
        Timber.d("OnMessageReceived Notification Body: ${remoteMessage.notification?.body}")
//        sendNotification(payload)
        parseNotification.parseFcmPayload(remoteMessage)
    }


    private fun sendNotificationToServer(token: String) {
        GlobalScope.launch {
            sendFcmTokenToServer.invoke(token).collect()
        }
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
        val channelDescription = getString(R.string.notifications_channel_description)

        val notificationChannel = NotificationChannel(
            getString(R.string.notification_channel_id),
            adminChannelName,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = channelDescription
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        return notificationChannel
    }
}
