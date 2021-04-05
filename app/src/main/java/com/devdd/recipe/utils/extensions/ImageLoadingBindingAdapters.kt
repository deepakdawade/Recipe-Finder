package com.devdd.exampleapp.commonui.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.Coil
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.transform.CircleCropTransformation
import com.devdd.recipe.utils.extensions.navigationIconView
import com.devdd.recipe.utils.extensions.px
import com.devdd.recipe.utils.extensions.toFileUri
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.Headers
import okhttp3.HttpUrl
import timber.log.Timber

/*
@BindingAdapter("shimmerAutoStart")
fun setShimmerDrawable(view: AppCompatImageView, enable: Boolean) {
    val shimmer = Shimmer.ColorHighlightBuilder()
        .setBaseColor(ContextCompat.getColor(view.context, R.color.grey_200))
        .setHighlightColor(ContextCompat.getColor(view.context, R.color.grey_100))
        .setHighlightAlpha(0.85f)
        .setBaseAlpha(0.95f)
        .setDuration(300)
        .setRepeatDelay(500)
        .setIntensity(0.1f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(enable)
        .build()
    val shimmerDrawable = ShimmerDrawable()
    shimmerDrawable.setShimmer(shimmer)
    view.setImageDrawable(shimmerDrawable)
}
*/

@BindingAdapter("loadAgentImage")
fun loadAgentImage(imageView: AppCompatImageView, path: String?) {
    path?.let {
        imageView.load(uri = it.toFileUri()) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }
}

@Suppress("CAST_NEVER_SUCCEEDS")
@BindingAdapter("loadNavIcon")
fun loadNavIcon(toolbar: MaterialToolbar, path: String?) {
    path?.let {
        val navIconView: ImageView? = toolbar.navigationIconView as? ImageView
        navIconView?.load(uri = it.toFileUri()) {
            size(36.px(toolbar.context).toInt())
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }
}

/*
@BindingAdapter(
    "storageReference",
    "child",
    "errorPlaceholder",
    "placeholder",
    "setRoundAsCircle",
    "enableCrossfade",
    "disableCache",
    "disableHardwareRendering",
    requireAll = false
)
fun loadFireStoreImage(
    imageView: AppCompatImageView,
    storageReference: StorageReference,
    path: String?,
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
) {
    imageView.loadFireStoreImage(
        storageReference = storageReference,
        path = path,
        errorPlaceHolder = errorPlaceHolder,
        placeHolder = placeHolder,
        isRoundAsCircle = isRoundAsCircle,
        enableCrossfade = enableCrossfade,
        disableCache = disableCache,
        disableHardwareRendering = disableHardwareRendering
    )
}

fun AppCompatImageView.loadFireStoreImage(
    storageReference: StorageReference,
    path: String?,
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
    onSuccess: (Task<Uri>) -> Unit = {},
    onError: (Exception) -> Unit = {}
) {
    if (path.isNullOrBlank().not()) {
        try {
            storageReference.child(requireNotNull(path)).downloadUrl.addOnCompleteListener {
                onSuccess(it)
                if (it.isSuccessful) {
                    loadImage(
                        imageView = this,
                        path = it.result.toString(),
                        errorPlaceHolder = errorPlaceHolder,
                        placeHolder = placeHolder,
                        isRoundAsCircle = isRoundAsCircle,
                        enableCrossfade = enableCrossfade,
                        disableCache = disableCache,
                        disableHardwareRendering = disableHardwareRendering
                    )
                } else {
                    loadImage(
                        imageView = this,
                        path = it.result.toString(),
                        errorPlaceHolder = errorPlaceHolder,
                        placeHolder = placeHolder,
                        isRoundAsCircle = isRoundAsCircle,
                        enableCrossfade = enableCrossfade,
                        disableCache = disableCache,
                        disableHardwareRendering = disableHardwareRendering
                    )
                }
            }.addOnFailureListener { onError(it) }
        } catch (e: Exception) {
            Timber.d(e)
            onError(e)
            loadImage(
                imageView = this,
                path = null,
                errorPlaceHolder = errorPlaceHolder,
                placeHolder = placeHolder,
                isRoundAsCircle = isRoundAsCircle,
                enableCrossfade = enableCrossfade,
                disableCache = disableCache,
                disableHardwareRendering = disableHardwareRendering
            )
        }
    } else {
        loadImage(
            imageView = this,
            path = null,
            errorPlaceHolder = errorPlaceHolder,
            placeHolder = placeHolder,
            isRoundAsCircle = isRoundAsCircle,
            enableCrossfade = enableCrossfade,
            disableCache = disableCache,
            disableHardwareRendering = disableHardwareRendering
        )
    }
}
*/


@BindingAdapter(
    "load",
    "errorPlaceholderRes",
    "placeholderRes",
    "setRoundAsCircle",
    "enableCrossfade",
    "disableCache",
    "disableHardwareRendering",
    requireAll = false
)
fun loadImageResource(
    imageView: AppCompatImageView,
    path: String?,
    errorPlaceHolderRes: Int? = null,
    placeHolderRes: Int? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
) {
    val errorPlaceHolder: Drawable? =
        errorPlaceHolderRes?.let { AppCompatResources.getDrawable(imageView.context, it) }
    val placeHolder: Drawable? =
        placeHolderRes?.let { AppCompatResources.getDrawable(imageView.context, it) }
    loadImage(
        imageView = imageView,
        path = path,
        errorPlaceHolder = errorPlaceHolder,
        placeHolder = placeHolder,
        isRoundAsCircle = isRoundAsCircle,
        enableCrossfade = enableCrossfade,
        disableCache = disableCache,
        disableHardwareRendering = disableHardwareRendering
    )
}

@BindingAdapter(
    "load",
    "errorPlaceholder",
    "placeholder",
    "setRoundAsCircle",
    "enableCrossfade",
    "disableCache",
    "disableHardwareRendering",
    requireAll = false
)
fun loadImage(
    imageView: AppCompatImageView,
    path: String?,
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false
) {
    imageView.loadImage(
        path = path,
        errorPlaceHolder = errorPlaceHolder,
        placeHolder = placeHolder,
        isRoundAsCircle = isRoundAsCircle,
        enableCrossfade = enableCrossfade,
        disableCache = disableCache,
        disableHardwareRendering = disableHardwareRendering
    )
}

fun AppCompatImageView.loadImage(
    path: String?,
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
    onStart: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val url = path as? HttpUrl //?.toHttpUrlOrNull()
    when {
        url != null -> load(url = url) {
            setDefaults(
                errorPlaceHolder = errorPlaceHolder,
                placeHolder = placeHolder,
                isRoundAsCircle = isRoundAsCircle,
                enableCrossfade = enableCrossfade,
                disableCache = disableCache,
                disableHardwareRendering = disableHardwareRendering,
                onStart = onStart,
                onSuccess = onSuccess,
                onError = onError,
                onCancel = onCancel
            )
        }
        else -> load(uri = path?.toFileUri()) {
            setDefaults(
                errorPlaceHolder = errorPlaceHolder,
                placeHolder = placeHolder,
                isRoundAsCircle = isRoundAsCircle,
                enableCrossfade = enableCrossfade,
                disableCache = disableCache,
                disableHardwareRendering = disableHardwareRendering,
                onStart = onStart,
                onSuccess = onSuccess,
                onError = onError,
                onCancel = onCancel
            )
        }
    }
}


private fun ImageRequest.Builder.setDefaults(
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
    onStart: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    crossfade(enableCrossfade)
    if (isRoundAsCircle) transformations(CircleCropTransformation())
    fallback(placeHolder)
    placeholder(placeHolder)
    error(errorPlaceHolder)
    if (disableCache) {
        diskCachePolicy(CachePolicy.DISABLED)
        memoryCachePolicy(CachePolicy.DISABLED)
        networkCachePolicy(CachePolicy.DISABLED)
    }
    allowHardware(disableHardwareRendering.not())
    listener(object : ImageRequest.Listener {
        override fun onCancel(request: ImageRequest) {
            super.onCancel(request)
            onCancel()
        }

        override fun onError(request: ImageRequest, throwable: Throwable) {
            super.onError(request, throwable)
            onError()
        }

        override fun onStart(request: ImageRequest) {
            super.onStart(request)
            onStart()
        }

        override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
            super.onSuccess(request, metadata)
            onSuccess()
        }
    })
}

@BindingAdapter(
    "dataUrl",
    "headers",
    "errorPlaceholderRes",
    "placeholderRes",
    "setRoundAsCircle",
    "enableCrossfade",
    "disableCache",
    "disableHardwareRendering",
    requireAll = false
)
fun loadDataUrlResource(
    imageView: AppCompatImageView,
    dataUrl: String,
    headers: Headers,
    errorPlaceHolderRes: Int? = null,
    placeHolderRes: Int? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
) {
    val errorPlaceHolder: Drawable? =
        errorPlaceHolderRes?.let { AppCompatResources.getDrawable(imageView.context, it) }
    val placeHolder: Drawable? =
        placeHolderRes?.let { AppCompatResources.getDrawable(imageView.context, it) }
    loadDataUrl(
        imageView,
        dataUrl,
        headers,
        errorPlaceHolder,
        placeHolder,
        isRoundAsCircle,
        enableCrossfade,
        disableCache,
        disableHardwareRendering
    )
}

@BindingAdapter(
    "dataUrl",
    "headers",
    "errorPlaceholder",
    "placeholder",
    "setRoundAsCircle",
    "enableCrossfade",
    "disableCache",
    "disableHardwareRendering",
    requireAll = false
)
fun loadDataUrl(
    imageView: AppCompatImageView,
    dataUrl: String,
    headers: Headers,
    errorPlaceHolder: Drawable? = null,
    placeHolder: Drawable? = null,
    isRoundAsCircle: Boolean = false,
    enableCrossfade: Boolean = true,
    disableCache: Boolean = false,
    disableHardwareRendering: Boolean = false,
) {
    val builder = ImageRequest.Builder(imageView.context)
        .data(dataUrl)
        .headers(headers)
        .crossfade(enableCrossfade)
        .allowHardware(disableHardwareRendering.not())
        .listener(
            onError = { _, throwable -> Timber.d(throwable) },
            onStart = { Timber.d("start") },
            onSuccess = { _, _ -> Timber.d("success") }
        )
        .target(imageView)

    if (isRoundAsCircle) builder.transformations(CircleCropTransformation())
    builder.error(errorPlaceHolder)
    builder.placeholder(placeHolder)

    if (disableCache) {
        builder.diskCachePolicy(CachePolicy.DISABLED)
        builder.memoryCachePolicy(CachePolicy.DISABLED)
        builder.networkCachePolicy(CachePolicy.DISABLED)
    }
    val imageRequest = builder.build()
    val imageLoader = Coil.imageLoader(imageView.context)
    imageLoader.enqueue(imageRequest)
}