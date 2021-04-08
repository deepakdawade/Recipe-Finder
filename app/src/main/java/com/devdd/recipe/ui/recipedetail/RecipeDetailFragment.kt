package com.devdd.recipe.ui.recipedetail

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.R
import com.devdd.recipe.base.MyFragment
import com.devdd.recipe.databinding.FragmentRecipeDetailBinding
import com.devdd.recipe.ui.home.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.toDataClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment :
    MyFragment<FragmentRecipeDetailBinding>(R.layout.fragment_recipe_detail) {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val args by navArgs<RecipeDetailFragmentArgs>()

    override fun onViewCreated(binding: FragmentRecipeDetailBinding, savedInstanceState: Bundle?) {
        val recipe = args.recipe.toDataClass<RecipeViewState>()
        binding.recipe = recipe

        binding.recipeDetailFragmentToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}