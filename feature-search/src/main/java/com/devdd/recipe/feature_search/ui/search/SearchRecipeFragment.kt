package com.devdd.recipe.feature_search.ui.search

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.base_android.utils.extensions.observeEvent
import com.devdd.recipe.feature_search.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.feature_search.databinding.FragmentSearchRecipeBinding
import com.devdd.recipe.feature_search.ui.search.adapter.SearchRecipeAdapter
import com.devdd.recipe.ui.utils.extensions.showSoftInput
import com.devdd.recipe.ui.utils.extensions.watchQueryTextChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipeFragment :
    DevFragment<FragmentSearchRecipeBinding>(R.layout.fragment_search_recipe) {

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel:SearchRecipeViewModel by viewModels()

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
        searchView?.onActionViewExpanded()
        searchView?.watchQueryTextChangeListener(viewModel.query)
        searchView?.showSoftInput()
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