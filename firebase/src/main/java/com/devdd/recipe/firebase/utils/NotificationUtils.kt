package com.devdd.recipe.firebase.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.devdd.recipe.firebase.R


// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, context: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // Build the notification
    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notification_channel_id)
    )

        .setSmallIcon(R.drawable.ic_dev_option)
        .setContentTitle(
            context
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)

        .setAutoCancel(true)


        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}