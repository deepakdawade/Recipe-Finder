package com.devdd.recipe.utils.bindingadapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager


@BindingAdapter("toggleVisibility")
fun View.toggleVisibility(shouldShow: Boolean) {
    isVisible = shouldShow
}

@BindingAdapter("stringVisibility")
fun View.stringVisibility(string: String?) {
    isVisible = !string.isNullOrBlank()
}


@BindingAdapter("hideWithAnimationIfNullAndBlank")
fun View.hideWithAnimationIfNullAndBlank(string: String?) {
    val visibility = string.isNullOrBlank()
    val constraintLayout = parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    isVisible = !visibility
}

@BindingAdapter("showWithAnimationIfNullAndBlank")
fun View.showWithAnimationIfNullAndBlank(string: String?) {
    val visibility = string.isNullOrBlank()
    val constraintLayout = parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    isVisible = visibility
}

@BindingAdapter("animateVisibility")
fun View.animateVisibility(visibility: Boolean) {
    val constraintLayout = parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    isVisible = visibility
}


@BindingAdapter("showOnData", "shouldShow", requireAll = false)
fun View.showOnData(response: String?, shouldShow: Boolean = true) {
    isVisible = !response.isNullOrBlank() && shouldShow
}


@BindingAdapter("hideGroupWhenLoading")
fun Group.hideGroupWhenLoading(isLoading: Boolean?) {
    val visibility = isVisible
    isVisible = when (isLoading) {
        false, null -> visibility
        true -> false
    }
}


@BindingAdapter("hideOnEmptyList")
fun View.showOnData(response: List<*>?) {
    isVisible = !response.isNullOrEmpty()
}

@BindingAdapter("hideEmptyScreen", "showEmptyScreen", "disableEmptyScreen", requireAll = false)
fun View.showEmptyScreen(
    isLoading: Boolean,
    data: List<*>?,
    disableEmptyScreen: Boolean = false
) {
    if (disableEmptyScreen) {
        visibility = View.GONE
        return
    }
    val visibility = if (isLoading || data.isNullOrEmpty().not()) View.GONE else View.VISIBLE
    if (visibility != visibility) post { this.visibility = visibility }
}


fun isValid(text: String, type: String) = when (type) {
    DIGIT_ONLY -> text.isBlank()
    else -> false
}

private const val DIGIT_ONLY = "digitOnly"
