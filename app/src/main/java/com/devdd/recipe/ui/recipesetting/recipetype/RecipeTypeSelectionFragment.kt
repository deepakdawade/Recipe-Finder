package com.devdd.recipe.ui.recipesetting.recipetype

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentRecipeTypeSelectionBinding
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeTypeSelectionFragment :
    DevFragment<FragmentRecipeTypeSelectionBinding>(R.layout.fragment_recipe_type_selection) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<RecipeTypeSelectionViewModel>()
    override fun onViewCreated(
        binding: FragmentRecipeTypeSelectionBinding,
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