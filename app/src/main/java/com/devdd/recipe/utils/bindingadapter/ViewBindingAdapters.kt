package com.devdd.recipe.utils.bindingadapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView


@BindingAdapter("toggleVisibility")
fun View.toggleVisibility(shouldShow: Boolean) {
    isVisible = shouldShow
}

@BindingAdapter("stringVisibility","animateVisibility",requireAll = false)
fun View.stringVisibility(string: String?,animate:Boolean? = false) {
    if (animate == true) {
        val layout = parent as? ViewGroup
        layout?.let { TransitionManager.beginDelayedTransition(it) }
    }
    isVisible = !string.isNullOrBlank()
}

@BindingAdapter("hideOnList", "hideOnEmpty")
fun View.showOnData(response: List<*>?, hideOnEmptyList: Boolean) {
    isVisible = if (hideOnEmptyList)
        !response.isNullOrEmpty()
    else
        response.isNullOrEmpty()
}

@BindingAdapter("cardBackgroundColor")
fun MaterialCardView.cardBackgroundColor(@ColorRes colorRes: Int) {
    setCardBackgroundColor(ContextCompat.getColorStateList(context, colorRes))
}
