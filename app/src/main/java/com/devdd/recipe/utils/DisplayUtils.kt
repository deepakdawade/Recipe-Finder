package com.devdd.recipe.utils

import android.content.res.Resources
import android.util.TypedValue


/**
 * @author Deepak Dawade
 */
object DisplayUtils {

    fun dpToPx(dp: Float): Float = dp * Resources.getSystem().displayMetrics.density

    fun pxToDp(px: Float): Float =
        px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / 160f)

    fun spToPx(sp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            Resources.getSystem().displayMetrics
        ).toInt()

    fun percentToPxWidth(percent: Double): Int =
        ((percent / 100) * Resources.getSystem().displayMetrics.widthPixels).toInt()

    fun percentToPxHeight(percent: Double): Int =
        ((percent / 100) * Resources.getSystem().displayMetrics.heightPixels).toInt()

}