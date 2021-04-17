package com.devdd.recipe.ui.search

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentSearchRecipeBinding
import com.devdd.recipe.ui.home.adapter.SearchRecipeAdapter
import com.devdd.recipe.utils.extensions.watchQueryTextChangeListener
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipeFragment :
    DevFragment<FragmentSearchRecipeBinding>(R.layout.fragment_search_recipe) {

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<SearchRecipeViewModel>()

    private var searchRecipeAdapter: SearchRecipeAdapter? = null

    override fun onViewCreated(binding: FragmentSearchRecipeBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setViews()
        setupRecyclerViewAdapter()
        setObserver()

    }

    private fun setViews() {
        val searchView =
            binding?.searchRecipeFragmentToolbar?.menu?.getItem(0)?.actionView as? SearchView
        searchView?.watchQueryTextChangeListener(viewModel.query)
    }

    private fun setupRecyclerViewAdapter() {
        searchRecipeAdapter = SearchRecipeAdapter(viewModel)
        binding?.searchRecipeFragmentSearchResults?.adapter = searchRecipeAdapter
    }

    private fun setObserver() {
        viewModel.recipes.observe(viewLifecycleOwner) {
            searchRecipeAdapter?.submitList(it)
        }
        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchRecipeAdapter = null
    }
}