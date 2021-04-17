package com.devdd.recipe.utils.bindingadapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.devdd.recipe.R
import com.devdd.recipe.utils.extensions.px

@BindingAdapter(
    "loadImage",
    "circleCrop",
    "roundCorner",
    "cornerRadius",
    requireAll = false
)
fun AppCompatImageView.loadImage(
    imageUrl: String?,
    circleCrop: Boolean? = false,
    roundCorner: Boolean? = false,
    cornerRadius: Int? = 0
) {
    load(imageUrl) {
        placeholder(R.drawable.ic_launcher_foreground)
        if (circleCrop == true)
            transformations(CircleCropTransformation())
        else if (roundCorner == true)
            transformations(RoundedCornersTransformation(radius = (cornerRadius ?: 0).px(context)))
        error(R.drawable.ic_launcher_foreground)
    }
}
