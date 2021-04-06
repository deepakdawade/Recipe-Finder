package com.devdd.recipe.utils.extensions

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.devdd.recipe.R

@BindingAdapter("loadImage")
fun AppCompatImageView.loadImage(imageUrl: String) {
    load(imageUrl) {
        placeholder(R.drawable.ic_launcher_foreground)
        error(R.drawable.ic_launcher_foreground)
    }
}