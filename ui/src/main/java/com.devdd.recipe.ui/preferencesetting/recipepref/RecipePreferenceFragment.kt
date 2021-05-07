package com.devdd.recipe.ui.preferencesetting.recipepref

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.devdd.recipe.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.databinding.FragmentRecipePreferenceBinding
import com.devdd.recipe.ui.preferencesetting.PreferenceSettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipePreferenceFragment :
    DevFragment<FragmentRecipePreferenceBinding>(R.layout.fragment_recipe_preference) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<PreferenceSettingViewModel>({ requireParentFragment() })
    override fun onViewCreated(
        binding: FragmentRecipePreferenceBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
    }
}