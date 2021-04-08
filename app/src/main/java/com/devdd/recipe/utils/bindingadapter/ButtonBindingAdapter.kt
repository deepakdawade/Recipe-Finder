package com.devdd.recipe.utils.bindingadapter

import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton


@BindingAdapter(
    "dataToCheck",
    "dataCheckType",
    "disableWhileLoading",
    requireAll = false
)
fun MaterialButton.isDataValid(
    dataToCheck: String?,
    dataCheckType: String,
    isLoading: Boolean?
) {
    isEnabled = false
    if (isLoading == true) return
    dataToCheck?.let {
        val shouldEnable: Boolean = isValid(it, dataCheckType)
        if (isEnabled != shouldEnable) isEnabled = shouldEnable
    }
}

@BindingAdapter("onDataValid", "includeNetworkState", requireAll = false)
fun MaterialButton.onDataValid(onDataValid: Boolean?, includeNetworkState: Boolean?) {
    isEnabled = false
    if (includeNetworkState == true) return
    onDataValid?.let { if (isEnabled != it) isEnabled = it }
}

@BindingAdapter("onCountDownFinish")
fun MaterialButton.onCountDownFinish(dataToCheck: Long?) {
    isEnabled = false
    dataToCheck?.let {
        val shouldEnable: Boolean = it == 0L
        if (isEnabled != shouldEnable) isEnabled = shouldEnable
    }
}

