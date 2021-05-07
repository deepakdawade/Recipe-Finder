package com.devdd.recipe.utils.extensions

import android.content.Context
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.devdd.recipe.base.result.Result
import com.devdd.recipe.ui.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


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
