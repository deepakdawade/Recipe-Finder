package com.devdd.recipe.ui.recipedetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentRecipeDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment :
    DevFragment<FragmentRecipeDetailBinding>(R.layout.fragment_recipe_detail) {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val viewModel: RecipeDetailViewModel by viewModels()
    private val args by navArgs<RecipeDetailFragmentArgs>()

    override fun onViewCreated(binding: FragmentRecipeDetailBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        viewModel.loadRecipeById(args.recipeId)
        binding.recipeDetailFragmentToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}