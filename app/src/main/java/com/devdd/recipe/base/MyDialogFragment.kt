package com.devdd.recipe.base

import androidx.fragment.app.DialogFragment

abstract class MyDialogFragment : DialogFragment() {

    @Suppress("PropertyName")
    val TAG: String = this::class.java.simpleName

}