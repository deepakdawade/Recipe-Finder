package com.devdd.recipe.ui.recipedetail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.base_android.utils.extensions.IntentPackages
import com.devdd.recipe.base_android.utils.extensions.lookUpPackages
import com.devdd.recipe.base_android.utils.localemanager.LocaleManagerUtils
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
        setListeners(binding)
    }

    private fun setListeners(binding: FragmentRecipeDetailBinding) {
        binding.recipeDetailFragmentBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recipeDetailFragmentShareWhatsapp.setOnClickListener {
            shareViaWhatsapp()
        }
    }

    private fun shareViaWhatsapp() {
        val recipe = viewModel.recipe.value?.entity ?: return
        val message =
            if (LocaleManagerUtils.isEnglishLocale(requireContext())) recipe.message else recipe.messageHi
        Intent(Intent.ACTION_SEND).also { whatsappIntent ->
            whatsappIntent.setPackage(IntentPackages.WHATSAPP)
            whatsappIntent.type = "text/plain"
            requireContext().lookUpPackages(whatsappIntent)?.also {
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(whatsappIntent)
            } ?: Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT)
                .show()
        }

    }
}