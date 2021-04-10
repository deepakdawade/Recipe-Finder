package com.devdd.recipe.ui.recipesetting.recipepref

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentRecipePreferenceSelectionBinding
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipePreferenceSelectionFragment :
    DevFragment<FragmentRecipePreferenceSelectionBinding>(R.layout.fragment_recipe_preference_selection) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<RecipePreferenceSelectionViewModel>()
    override fun onViewCreated(
        binding: FragmentRecipePreferenceSelectionBinding,
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