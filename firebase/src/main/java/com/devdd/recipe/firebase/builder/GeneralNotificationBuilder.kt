package com.devdd.recipe.firebase.builder

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.text.SpannableString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.base_android.utils.extensions.executeImageRequest
import com.devdd.recipe.base_android.utils.extensions.getResourceUri
import com.devdd.recipe.firebase.NotifyParsedNotification
import com.devdd.recipe.firebase.R
import com.devdd.recipe.firebase.channels.RecipeNotificationChannels
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GeneralNotificationBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeNotificationChannels: RecipeNotificationChannels,
    private val notificationManager: NotificationManagerCompat,
    private val dispatchers: AppCoroutineDispatchers,
) {

    fun createAndNotify(notificationTrayItems: NotifyParsedNotification.NotificationTrayItems, bitmap: Bitmap? = null) {
        when {
            notificationTrayItems.imageUrl.isNullOrBlank().not() -> {
                GlobalScope.launch(dispatchers.main) {
                    val drawable =
                        context.executeImageRequest(notificationTrayItems.imageUrl?.toUri())
                    val notification =
                        createNotification(notificationTrayItems, drawable?.toBitmap())
                    notificationManager.notify(notificationTrayItems.notifyId, notification)
                }
            }
            else -> {
                val notification = createNotification(notificationTrayItems, bitmap)
                notificationManager.notify(notificationTrayItems.notifyId, notification)
            }
        }
    }

    fun createNotificationBuilder(
        notificationTrayItems: NotifyParsedNotification.NotificationTrayItems,
        bitmap: Bitmap?,
    ): NotificationCompat.Builder {
        val title = SpannableString(notificationTrayItems.title.parseAsHtml())
        val message = SpannableString(notificationTrayItems.msg.parseAsHtml())

        val notificationBuilder =
            NotificationCompat.Builder(context, notificationTrayItems.channel.channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_dev_option)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))

        notificationTrayItems.deeplink?.let { notificationBuilder.setContentIntent(it) }

        if (notificationTrayItems.isHeadsUp) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
        } else {
            notificationBuilder.setPriority(NotificationCompat.DEFAULT_ALL)
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
        }

        if (recipeNotificationChannels.isChannelImportanceHigh(notificationTrayItems.channel.channelId)) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
        }

        notificationTrayItems.soundResource?.let { rawRes ->
            val soundUri = context.getResourceUri(rawRes)
            notificationBuilder.setSound(soundUri)
        }

        if (bitmap != null) {
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle(title)
            )
        } else {
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(message)
            )
        }

        return notificationBuilder
    }

    private fun createNotification(
        notificationTrayItems: NotifyParsedNotification.NotificationTrayItems,
        bitmap: Bitmap?,
    ): Notification = createNotificationBuilder(notificationTrayItems, bitmap).build()
}