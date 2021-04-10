package com.devdd.recipe.ui.preferencesetting.recipepref

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentRecipePreferenceBinding
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipePreferenceFragment :
    DevFragment<FragmentRecipePreferenceBinding>(R.layout.fragment_recipe_preference) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<RecipePreferenceViewModel>()
    override fun onViewCreated(
        binding: FragmentRecipePreferenceBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            if (it.second)
                findNavController().navigateUp()
            else
                findNavController().navigate(it.first)
        }
    }
}