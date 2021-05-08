package com.devdd.recipe.ui.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


inline fun <T : ViewDataBinding> bindWithLayout(
    @LayoutRes layoutId: Int,
    parent: ViewGroup,
    attachToRoot: Boolean = false,
    bind: (T.() -> Unit) = {}
): T {
    val inflater: LayoutInflater = LayoutInflater.from(parent.context)
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, parent, attachToRoot)
    binding.bind()
    return binding
}

inline fun <T : ViewDataBinding> bindWithLayout(
    @LayoutRes layoutId: Int,
    inflater: LayoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false,
    bind: (T.() -> Unit) = {}
): T {
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, parent, attachToRoot)
    binding.bind()
    return binding
}

inline fun <T : ViewDataBinding> bindWithLayout(
    @LayoutRes layoutId: Int,
    context: Context,
    bind: (T.() -> Unit) = {}
): T {
    val view = View.inflate(context, layoutId, null)
    val binding = requireNotNull(DataBindingUtil.bind<T>(view))
    binding.bind()
    return binding
}
