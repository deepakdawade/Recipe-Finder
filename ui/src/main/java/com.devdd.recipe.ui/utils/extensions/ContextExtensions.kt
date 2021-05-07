package com.devdd.recipe.utils.extensions

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.text.Spanned
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.ViewDataBinding
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.devdd.recipe.R
import com.devdd.recipe.domain.result.Result
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber
import java.util.*
import kotlin.random.Random


fun Context.toDimensions(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
}

fun Context.getDisplaySize(): DisplaySize {
    return resources.displayMetrics.run { DisplaySize(widthPixels, heightPixels) }
}

fun Int.dp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}

fun Int.px(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

@ColorInt
fun randomColor(): Int {
    return Color.argb(128, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}

fun Context.stringByLocale(id: Int, locale: Locale = Locale.ENGLISH): String {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration).resources.getString(id)
}

fun Context.resourcesByLocale(locale: Locale = Locale.ENGLISH): Resources {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration).resources
}

fun Context.getResourceId(name: String, type: String): Int {
    return resources.getIdentifier(name, type, packageName)
}

const val TESTING_CHANNEL_NAME: String = "Testing Purpose"
const val TESTING_CHANNEL_ID: String = "testing"
const val TESTING_CHANNEL_DESC: String = "For testing purpose only"

fun Context.testNotification() {
    // Build the notification and add the action.

    val newMessageNotification = NotificationCompat.Builder(this, TESTING_CHANNEL_NAME)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Hello World")
        .setContentText("This is wonderful")
        .build()

    // Issue the notification.
    with(NotificationManagerCompat.from(this)) {
        notify(TESTING_CHANNEL_ID.hashCode(), newMessageNotification)
    }
}

fun Context.pluralString(
    quantity: Int?,
    @StringRes zeroMsgResId: Int,
    @StringRes noneMsgResId: Int,
    @PluralsRes pluralResId: Int,
): String {
    return when (quantity) {
        null -> getString(noneMsgResId)
        0 -> getString(zeroMsgResId)
        else -> resources.getQuantityString(pluralResId, quantity, quantity)
    }
}

fun Context.spannedPluralString(
    quantity: Int?,
    @StringRes zeroMsgResId: Int,
    @StringRes noneMsgResId: Int = zeroMsgResId,
    @PluralsRes pluralResId: Int,
): Spanned {
    return HtmlCompat.fromHtml(
        pluralString(quantity, zeroMsgResId, noneMsgResId, pluralResId),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

fun Context.lookUpPackages(
    packageIntent: Intent,
    preferIntentType: Boolean = false,
    packages: List<String> = listOf(IntentPackages.WHATSAPP, IntentPackages.WHATSAPP_FOR_BUISNESS),
): ComponentName? {
    if (preferIntentType) return packageIntent.resolveActivity(packageManager)
    else for (pkg in packages) {
        packageIntent.`package` = pkg
        val resolvedComponentName = packageIntent.resolveActivity(packageManager)
        if (resolvedComponentName != null) return resolvedComponentName
    }
    return null
}

object IntentPackages {
    const val MAPS: String = "com.google.android.apps.maps"
    const val CHROME: String = "com.android.chrome"
    const val WEB_VIEW: String = "android.software.webview"
    const val WHATSAPP: String = "com.whatsapp"
    const val WHATSAPP_FOR_BUISNESS: String = "com.whatsapp.w4b"
}

data class DisplaySize(
    @Px val width: Int,
    @Px val height: Int,
)

fun Context.lastUpdateTime(): Long? {
    return try {
        packageManager.getPackageInfo(packageName, 0).lastUpdateTime
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}

fun Context.firstUpdateTime(): Long? {
    return try {
        packageManager.getPackageInfo(packageName, 0).firstInstallTime
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}

/**
 * @param resourceId identifies an application resource
 * @return the Uri by which the application resource is accessed
 */
fun Context.getResourceUri(@RawRes resourceId: Int): Uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(packageName)
    .path(resourceId.toString())
    .build()

fun Context.createMaterialAlertDialog(
    @StringRes title: Int = R.string.empty_string,
    @StringRes message: Int = R.string.empty_string,
    @StringRes negativeActionMsg: Int = R.string.empty_string,
    @StringRes positiveActionMsg: Int = R.string.empty_string,
    positiveAction: () -> Unit = {},
    negativeAction: () -> Unit = {},
    @LayoutRes layoutId: Int? = null,
    @DrawableRes iconId: Int? = null,
    cancelable: Boolean = true,
    viewBinding: ViewDataBinding? = null,
): AlertDialog {
    val dialogBuilder = MaterialAlertDialogBuilder(this).setCancelable(cancelable)

    if (title != R.string.empty_string) dialogBuilder.setTitle(title)

    if (message != R.string.empty_string) dialogBuilder.setMessage(message)

    if (negativeActionMsg != R.string.empty_string) {
        dialogBuilder.setNegativeButton(negativeActionMsg) { dialog, _ ->
            negativeAction()
            dialog.cancel()
        }
    }

    if (positiveActionMsg != R.string.empty_string) {
        dialogBuilder.setPositiveButton(positiveActionMsg) { dialog, _ ->
            positiveAction()
            dialog.cancel()
        }
    }

    if (layoutId != null) dialogBuilder.setView(layoutId)
    else if (viewBinding != null) dialogBuilder.setView(viewBinding.root)

    if (iconId != null) dialogBuilder.setIcon(iconId)

    return dialogBuilder.create()
}

inline fun <B : ViewDataBinding> Context.createBottomSheetDialog(
    @LayoutRes layoutId: Int,
    onBind: (B, BottomSheetDialog) -> Unit
): BottomSheetDialog {
    val dialog = BottomSheetDialog(this)
    bindWithLayout<B>(layoutId = layoutId, context = this) {
        onBind(this, dialog)
        dialog.setContentView(root)
    }
    return dialog
}


fun Context.loadLottieFromUrl(url: String, result: (Result<LottieDrawable>) -> Unit) {
    val lottieDrawable = LottieDrawable()
    LottieCompositionFactory.fromUrl(this@loadLottieFromUrl, url)
        .addListener {
            lottieDrawable.composition = it
            result(Result.Success(lottieDrawable))
        }.addFailureListener {
            result(Result.Error(Exception(it)))
        }
}

fun Context.themedColor(@AttrRes attrRes: Int): Int =
    MaterialColors.getColor(this, attrRes, "No Color defined in current theme")
