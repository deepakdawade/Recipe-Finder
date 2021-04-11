package com.devdd.recipe.ui.categoryrecipe

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.databinding.FragmentRecipesBinding
import com.devdd.recipe.ui.home.adapter.RecipeAdapter
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : DevFragment<FragmentRecipesBinding>(R.layout.fragment_recipes) {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val args by navArgs<RecipesFragmentArgs>()

    private val viewModel by viewModels<RecipesViewModel>()

    private var recipeAdapter: RecipeAdapter? = null

    override fun onViewCreated(binding: FragmentRecipesBinding, savedInstanceState: Bundle?) {
        binding.recipesViewModel = viewModel
        viewModel.fetchRecipes(RecipesByCategoryRequest(args.categoryId))
        setView(binding)
        setListeners(binding)
        setupRecyclerViewAdapter(binding)
        setObservers()
    }

    private fun setView(binding: FragmentRecipesBinding) {
        binding.recipesFragmentToolbar.title = args.categoryName
        binding.recipesFragmentLottieNoRecipes.setAnimation(R.raw.recipe_not_found_animation)
    }

    private fun setListeners(binding: FragmentRecipesBinding) {
        binding.recipesFragmentToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerViewAdapter(binding: FragmentRecipesBinding) {
        recipeAdapter = RecipeAdapter {
            viewModel.navigateToRecipeDetails(it)
        }
        binding.recipesFragmentRecipeList.adapter = recipeAdapter
    }

    private fun setObservers() {
        viewModel.recipes.observe(viewLifecycleOwner) {
            recipeAdapter?.submitList(it)
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
    }


}