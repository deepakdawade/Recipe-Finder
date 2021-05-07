package com.devdd.recipe.utils.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> T.toJsonString(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.toDataClass(): T {
    return Gson().fromJson(this, object : TypeToken<T>() {}.type)
}