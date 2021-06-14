package com.devdd.recipe.firebase

import android.app.PendingIntent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.devdd.recipe.firebase.builder.GeneralNotificationBuilder
import com.devdd.recipe.firebase.channels.RecipeNotificationChannels
import javax.inject.Inject

class NotifyParsedNotification @Inject constructor(
    private val generalNotificationBuilder: GeneralNotificationBuilder
) {

    fun notify(notificationTrayItems: NotificationTrayItems) {
        generalNotificationBuilder.createAndNotify(notificationTrayItems)
    }

    fun notifyInAppNotifications(notificationTrayItems: NotificationTrayItems, bitmap: Bitmap?) {
        generalNotificationBuilder.createAndNotify(notificationTrayItems, bitmap)
    }

    fun createInAppNotificationBuilder(
        notificationTrayItems: NotificationTrayItems,
        bitmap: Bitmap?
    ): NotificationCompat.Builder {
        return generalNotificationBuilder.createNotificationBuilder(notificationTrayItems, bitmap)
    }

    data class NotificationTrayItems(
        val title: String,
        val msg: String,
        val imageUrl: String?,
        val channel: RecipeNotificationChannels.Channel,
        val soundResource: Int?,
        val deeplink: PendingIntent?,
        val isHeadsUp: Boolean,
        val notifyId: Int
    )
}