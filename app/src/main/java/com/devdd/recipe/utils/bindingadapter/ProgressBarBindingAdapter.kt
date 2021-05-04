package com.devdd.recipe.utils.bindingadapter

import android.content.res.ColorStateList
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.devdd.recipe.utils.extensions.animateProgress

@BindingAdapter("materialStyleProgress")
fun ProgressBar.materialStyleProgress(colorResIds: IntArray) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 8f
    circularProgressDrawable.setColorSchemeColors(*colorResIds)
    indeterminateDrawable = circularProgressDrawable
}

@BindingAdapter("tintOnIncomplete", "tintOnComplete", "determinateProgress")
fun ProgressBar.changeWhenComplete(
    tintOnIncomplete: Int,
    tintOnComplete: Int,
    determinateProgress: Int?
) {
    animateProgress(determinateProgress ?: 0)
    progressTintList = if (determinateProgress ?: 0 >= 100)
        ColorStateList.valueOf(tintOnComplete)
    else ColorStateList.valueOf(tintOnIncomplete)
}


@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.setRefreshState(response: Boolean?) {
    isRefreshing = response == true
}
