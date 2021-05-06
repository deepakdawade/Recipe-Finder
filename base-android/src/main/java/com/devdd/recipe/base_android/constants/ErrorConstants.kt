@file:Suppress("SpellCheckingInspection")

package com.devdd.recipe.base_android.constants

import androidx.annotation.StringRes
import com.devdd.recipe.base_android.R

private const val EINTERNAL = 0x10000002


private val errorMsgMapping: HashMap<Int, Int> = hashMapOf<Int, @StringRes Int>().apply {
    put(EINTERNAL, R.string.EINTERNAL)
}

fun getLocalizedErrorMessage(errorCode: Int): Int? {
    return errorMsgMapping[errorCode]
}