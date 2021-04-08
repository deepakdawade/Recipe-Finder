package com.devdd.recipe.utils.bindingadapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.devdd.recipe.R

@BindingAdapter("loadImage", "roundImage",requireAll = false)
fun AppCompatImageView.loadImage(imageUrl: String, isRoundImage: Boolean? = false) {
    load(imageUrl) {
        placeholder(R.drawable.ic_launcher_foreground)
        if (isRoundImage == true)
            transformations(CircleCropTransformation())
        error(R.drawable.ic_launcher_foreground)
    }
}
