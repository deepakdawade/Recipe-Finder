package com.devdd.recipe.firebase.constant

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.devdd.recipe.base.utils.RecipeAppConfig
import com.devdd.recipe.data.utils.toDataClass
import com.devdd.recipe.data.utils.toJsonString
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonElement
import java.util.HashMap

fun RemoteMessage.toNotificationPayload(): NotificationPayload {
    val remoteMessageTree: JsonElement = Gson().toJsonTree(data)
    return remoteMessageTree.toJsonString().toDataClass()
}

fun pendingIntentForAction(
    context: Context,
    notificationPayload: NotificationPayload,
): PendingIntent? {
    val notifyIntent = notificationPayload.action.let {
        val deepLink = notificationDeeplink(notificationPayload.action, notificationPayload)
        context.createNotifyIntent(notificationPayload.opsnid, deepLink)
    }
    return PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
}
private val actionDestinations: HashMap<String, String> = hashMapOf()
fun notificationDeeplink(action: String?, notificationPayload: NotificationPayload): Uri? {
    if (action.isNullOrBlank()) return notificationPayload.externalLink?.toUri()

    val deeplink: String = actionDestinations[action] ?: return null
    val args = deeplinkArgs(notificationPayload, action)
    return Uri.parse(deeplink + args)
}

private fun deeplinkArgs(notificationPayload: NotificationPayload?, action: String): String {
    return  ""
}

const val NOTIFICATION_OPS_NID: String = "opsnid"
private fun Context.createNotifyIntent(opsnid: Long?, deepLink: Uri?): Intent {
    return Intent(this, RecipeAppConfig.mainActivityClassName).apply {
        data = deepLink
        opsnid?.let { putExtra(NOTIFICATION_OPS_NID, it) }
    }
}