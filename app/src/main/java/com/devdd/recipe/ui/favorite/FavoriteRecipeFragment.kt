package com.devdd.recipe.ui.favorite

import android.os.Bundle
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentFavoriteRecipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipeFragment :
    DevFragment<FragmentFavoriteRecipeBinding>(R.layout.fragment_favorite_recipe) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun onViewCreated(
        binding: FragmentFavoriteRecipeBinding,
        savedInstanceState: Bundle?
    ) {

    }
}