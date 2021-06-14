package com.devdd.recipe.firebase.parse

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.devdd.recipe.base.utils.extensions.debugOnly
import com.devdd.recipe.base_android.utils.extensions.buildType
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.firebase.NotifyParsedNotification
import com.devdd.recipe.firebase.NotifyParsedNotification.NotificationTrayItems
import com.devdd.recipe.firebase.channels.RecipeNotificationChannels
import com.devdd.recipe.firebase.constant.NotificationPayload
import com.devdd.recipe.firebase.constant.pendingIntentForAction
import com.devdd.recipe.firebase.constant.toNotificationPayload
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParseNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeNotificationChannels: RecipeNotificationChannels,
    private val notifyParsedNotification: NotifyParsedNotification,
    private val localeManager: LocaleManager,
) {

    private val notifyId: AtomicInteger = AtomicInteger(NOTIFICATION_INITIAL_COUNTER)
    private fun getNotificationID(): Int = notifyId.incrementAndGet()

    private val notificationTrayMap = hashMapOf<String, NotificationTrayItems>()

    fun parseInAppNotifications(
        notifyId: Int,
        channelId: String,
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        pendingIntent: PendingIntent,
        bitmap: Bitmap?
    ) {
        val channel = recipeNotificationChannels.getChannel(channelId)
        val title = context.getString(titleRes)
        val message = context.getString(messageRes)

        val notificationTrayItems = NotificationTrayItems(
            title = title,
            msg = message,
            channel = channel,
            imageUrl = null,
            isHeadsUp = true,
            deeplink = pendingIntent,
            notifyId = notifyId,
            soundResource = recipeNotificationChannels.getSoundResource(channel.channelId)
        )
        notifyParsedNotification.notifyInAppNotifications(notificationTrayItems, bitmap)
    }

    fun createInAppNotifications(
        notifyId: Int,
        channelId: String,
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        pendingIntent: PendingIntent,
        bitmap: Bitmap?
    ): NotificationCompat.Builder {
        val channel = recipeNotificationChannels.getChannel(channelId)
        val title = context.getString(titleRes)
        val message = context.getString(messageRes)

        val notificationTrayItems = NotificationTrayItems(
            title = title,
            msg = message,
            channel = channel,
            imageUrl = null,
            isHeadsUp = true,
            deeplink = pendingIntent,
            notifyId = notifyId,
            soundResource = null
        )
        return notifyParsedNotification.createInAppNotificationBuilder(
            notificationTrayItems,
            bitmap
        )
    }

    fun parseFcmPayload(remoteMessage: RemoteMessage) {
        try {

            val notificationPayload = remoteMessage.toNotificationPayload()

            buildType.debugOnly {
                Handler(Looper.getMainLooper()).post {
                    val toastMsg =
                        "${notificationPayload.action}, silent : ${notificationPayload.silent}"
                    Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                }
            }
            parseNotification(notificationPayload)
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }


    private fun parseNotification(notificationPayload: NotificationPayload) {
        parseGeneralNotification(notificationPayload)
    }

    private fun parseGeneralNotification(notificationPayload: NotificationPayload) {
        val channel = recipeNotificationChannels.getChannel(notificationPayload.channelId)
        val title = notificationPayload.title
        val message = notificationPayload.message
        val isHeadsUp = recipeNotificationChannels.isHeadsUpNotification(channel.channelId)

        if (title.isNullOrBlank() || message.isNullOrBlank()) {
            return
        }
        val alreadyNotifiedItems = notificationTrayMap[notificationPayload.action]
        val notifyId: Int = alreadyNotifiedItems?.let {
            if (it.title == title && it.msg == message) it.notifyId
            else getNotificationID()
        } ?: getNotificationID()

        val notificationTrayItems = NotificationTrayItems(
            title = title,
            msg = message,
            channel = channel,
            imageUrl = notificationPayload.imageURL,
            isHeadsUp = isHeadsUp,
            deeplink = pendingIntentForAction(context, notificationPayload),
            notifyId = notifyId,
            soundResource = recipeNotificationChannels.getSoundResource(channel.channelId)
        )

        processNotificationActions(notificationPayload)
        notificationTrayMap[notificationPayload.action] = notificationTrayItems
        notifyParsedNotification.notify(notificationTrayItems)
    }

    private fun processNotificationActions(notificationPayload: NotificationPayload) {
        when (notificationPayload.action) {

        }
    }

    companion object {

        private val TAG = ParseNotification::class.java.simpleName

        private const val NOTIFICATION_INITIAL_COUNTER = 10
    }

}