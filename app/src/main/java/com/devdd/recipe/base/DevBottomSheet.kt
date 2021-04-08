package com.devdd.recipe.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

abstract class DevBottomSheet<V : ViewDataBinding> : BottomSheetDialogFragment() {

    var binding: V? = null

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

    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): V

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(requireBinding(), savedInstanceState)
        view.clipToOutline = true
    }

    abstract fun onViewCreated(binding: V, savedInstanceState: Bundle?)

    @Suppress("PropertyName")
    val TAG: String = this::class.java.simpleName

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}