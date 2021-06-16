package com.devdd.recipe.ui.utils.extensions

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

inline fun <reified T : AppCompatActivity> AppCompatActivity.finishAndLaunch(
    noinline with: (Intent.() -> Intent) = { Intent() }
) {
    with(Intent()).setClass(this, T::class.java).also {
        startActivity(it)
    }
    finish()
}

inline fun <reified T : ViewDataBinding> AppCompatActivity.bindingWithLifecycleOwner(
    @LayoutRes layoutId: Int,
    bind: (T.() -> Unit) = {}
): T {

    val binding: T = DataBindingUtil.setContentView(this, layoutId)
    binding.lifecycleOwner = this
    binding.bind()
    return binding
}

fun Activity.hideSoftInput() {
    val imm: InputMethodManager? = getSystemService()
    val currentFocus = currentFocus
    if (currentFocus != null && imm != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}