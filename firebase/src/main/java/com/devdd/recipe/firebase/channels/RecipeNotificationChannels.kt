package com.devdd.recipe.firebase.channels

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.devdd.recipe.base.utils.extensions.debugOnly
import com.devdd.recipe.base_android.utils.extensions.*
import com.devdd.recipe.firebase.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeNotificationChannels @Inject constructor(
    private val notificationManager: NotificationManagerCompat,
    @ApplicationContext private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannels() {
        removedChannelIds.forEach { id -> notificationManager.deleteNotificationChannel(id) }

        channelsIdMap.forEach { (_, channel) ->
            val importance =
                if (isChannelImportanceHigh(channel.channelId)) IMPORTANCE_HIGH else IMPORTANCE_DEFAULT

            val soundResource: Int? = channelIdToSoundResMap[channel.channelId]

            val newChannel = createNotificationChannel(
                channelId = channel.channelId,
                channelName = channel.channelName,
                channelDesc = channel.channelDesc,
                importance = importance,
                soundResource = soundResource
            )

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(newChannel)
        }

        buildType.debugOnly {
            createNotificationChannel(
                channelId = TESTING_CHANNEL_ID,
                channelName = TESTING_CHANNEL_NAME,
                channelDesc = TESTING_CHANNEL_DESC,
                importance = IMPORTANCE_HIGH,
                soundResource = null
            )
        }
    }

    fun isChannelImportanceHigh(channelId: String): Boolean {
        return channelIdToSoundResMap.containsKey(channelId)
    }

    fun getChannel(channelID: String?): Channel {
        return channelsIdMap[channelID] ?: requireNotNull(channelsIdMap[GENERAL_SERVER_CHANNEL_ID])
    }

    fun isHeadsUpNotification(channelID: String): Boolean {
        return channelID != GENERAL_CHANNEL_ID
    }

    fun getSoundResource(channelId: String): Int? = channelIdToSoundResMap[channelId]

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDesc: String,
        importance: Int,
        soundResource: Int?,
    ): NotificationChannel {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDesc
        channel.setShowBadge(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        soundResource?.let { rawResource ->
            val soundUri = context.getResourceUri(rawResource)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)
        }
        return channel
    }

    companion object {

        // removed channel id
        private val removedChannelIds = listOf("General")

        // server channel id
        private const val GENERAL_SERVER_CHANNEL_ID = "general"

        // channel id
        private const val GENERAL_CHANNEL_ID = "general_v1"

        // channels name
        private const val GENERAL_CHANNEL_NAME = "General"

        // channel description
        private const val GENERAL_CHANNEL_DESC = "Recipe useful information & updates"

        private val channelsIdMap = hashMapOf(
            GENERAL_SERVER_CHANNEL_ID to Channel(
                channelId = GENERAL_CHANNEL_ID,
                channelName = GENERAL_CHANNEL_NAME,
                channelDesc = GENERAL_CHANNEL_DESC
            )
        )

        private val channelIdToSoundResMap = mapOf(
            // Sound for general notification
            GENERAL_CHANNEL_ID to R.raw.general_notification_8khz_good_quality
        )
    }

    data class Channel(val channelId: String, val channelName: String, val channelDesc: String)
}