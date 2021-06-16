package com.devdd.recipe.ui.base

import androidx.fragment.app.DialogFragment

abstract class DevDialogFragment : DialogFragment() {

    @Suppress("PropertyName")
    val TAG: String = this::class.java.simpleName

}