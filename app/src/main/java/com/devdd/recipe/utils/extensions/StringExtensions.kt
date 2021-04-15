package com.devdd.recipe.utils.extensions


/**
 * Created by @author Deepak Dawade on 4/16/2021 at 12:34 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
/**
 * Returns `true` if this string is not `null` and its content is numeric, ignoring case, and `false` otherwise.
 * */
fun String?.isInt(): Boolean = this?.toIntOrNull() != null

/**
 * Returns `true` if this string is not `null` and its content is is equal to the word "true", ignoring case, and `false` otherwise.
 * */
fun String?.isBoolean(): Boolean = this.toBooleanOrNull() != null

/**
 * Returns `true` if this string is not `null` and its content is equal to the word "true", ignoring case, and `false` otherwise.
 * */
fun String?.toBooleanOrNull() = if (this == "true" || this == "false") true else null