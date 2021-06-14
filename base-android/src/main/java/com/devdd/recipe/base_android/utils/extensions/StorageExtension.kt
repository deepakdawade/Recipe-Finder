package com.devdd.recipe.base_android.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

suspend fun Context.executeImageRequest(
    data: Any?,
    placeholder: Drawable? = null,
    errorPlaceHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = true,
): Drawable? {
    val requestBuilder = ImageRequest.Builder(this)
        .data(data)
        .placeholder(placeholder)
        .error(errorPlaceHolder)
        .crossfade(enableCrossfade)
        .allowHardware(disableHardwareRendering.not())

    if (disableCache) {
        requestBuilder.diskCachePolicy(CachePolicy.DISABLED)
        requestBuilder.memoryCachePolicy(CachePolicy.DISABLED)
        requestBuilder.networkCachePolicy(CachePolicy.DISABLED)
    }

    if (isRoundAsCircle) requestBuilder.transformations(CircleCropTransformation())
    return Coil.imageLoader(this).execute(requestBuilder.build()).drawable
}
