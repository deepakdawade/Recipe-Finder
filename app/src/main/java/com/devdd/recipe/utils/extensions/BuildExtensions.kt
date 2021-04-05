package com.devdd.recipe.utils.extensions

fun BuildType.debugOnly(whenDebugging: () -> Unit) {
    if (this == BuildType.DEBUG) {
        whenDebugging()
    }
}

fun BuildType.releaseOnly(onProduction: () -> Unit) {
    if (this == BuildType.RELEASE) {
        onProduction()
    }
}

inline fun <reified T> BuildType.debugElseRelease(
    whenDebugging: () -> T,
    onProduction: () -> T,
): T {
    return if (this == BuildType.DEBUG) {
        whenDebugging()
    } else {
        onProduction()
    }
}

enum class BuildType {
    DEBUG,
    RELEASE
}