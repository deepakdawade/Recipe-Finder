package com.devdd.recipe.utils.extensions

import kotlinx.coroutines.Job

/**
 * Cancel the Job if it's active.
 */
fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}