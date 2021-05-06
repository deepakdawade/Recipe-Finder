package com.devdd.recipe.base_android.utils.extensions

import android.os.Build
import com.devdd.recipe.base.utils.extensions.BuildType
import com.devdd.recipe.base_android.BuildConfig

fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}

infix fun Boolean.then(fix: () -> Unit) {
    if (this) fix()
}

val buildType: BuildType = when (BuildConfig.BUILD_TYPE) {
    "debug" -> BuildType.DEBUG
    "release" -> BuildType.RELEASE
    else -> throw IllegalStateException("unknown build type ${BuildConfig.BUILD_TYPE}")
}

enum class CallerFragmentType {
    BOTTOM_NAVIGATION,
    PARENT_FRAGMENT,
    VIEW_PAGER_FRAGMENT,
    PARENT_ACTIVITY
}