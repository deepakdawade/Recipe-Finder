package com.devdd.recipe.utils.bindingadapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.devdd.recipe.utils.extensions.animateAVD

@BindingAdapter("avdLoop")
fun AppCompatImageView.animateAVD(loop: Boolean) {
    animateAVD(loop)
}

@BindingAdapter("srcVectorRes")
fun AppCompatImageView.setSrcVectorRes(@DrawableRes drawable: Int) {
    setImageResource(drawable)
}

@BindingAdapter("srcVectorDrawable")
fun AppCompatImageView.setSrcVectorDrawable(drawable: Drawable) {
    setImageDrawable(drawable)
}


@BindingAdapter("imageTint")
fun AppCompatImageView.tintColorName(color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}
