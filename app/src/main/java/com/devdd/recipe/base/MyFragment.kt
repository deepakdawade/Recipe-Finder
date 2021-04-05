package com.devdd.recipe.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class MyFragment<V : ViewDataBinding> : Fragment() {

    var binding: V? = null

    private var snackbar: Snackbar? = null

    @Suppress("PropertyName")
    val TAG: String = this::class.java.simpleName

    fun requireBinding(): V = requireNotNull(binding)

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return createBinding(inflater, container)
            .also { binding = it }
            .root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(requireBinding(), savedInstanceState)
    }

    abstract fun onViewCreated(binding: V, savedInstanceState: Bundle?)

    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): V

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        if (snackbar?.isShown == true) snackbar?.dismiss()
        snackbar = null
    }
}