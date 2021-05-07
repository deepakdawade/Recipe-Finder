package com.devdd.recipe.utils.navigation

import android.net.Uri

data class NavigateOnceDeeplinkRequest(
    // The deeplink of the destination
    val deeplink: Uri,

    // Defines the behavior of multiple continuous instances to be allowed or not for a destination.
    // true: allows continuous instances of a same fragment.
    // false: do not allow continuous instances of same fragment.
    val allowMultipleInstance: Boolean = false,

    //  if set to true fragment will be popped from backStack and navigate
    // else set to false backStackEntry will be used with old arguments if any
    val updateArguments: Boolean = true
)
 