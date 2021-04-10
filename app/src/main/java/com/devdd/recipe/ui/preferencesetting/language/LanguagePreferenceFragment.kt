package com.devdd.recipe.ui.preferencesetting.language

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentLanguagePreferenceBinding
import com.devdd.recipe.utils.extensions.observeEvent

class LanguagePreferenceFragment :
    DevFragment<FragmentLanguagePreferenceBinding>(R.layout.fragment_language_preference) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<LanguagePreferenceViewModel>()
    override fun onViewCreated(
        binding: FragmentLanguagePreferenceBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }
}