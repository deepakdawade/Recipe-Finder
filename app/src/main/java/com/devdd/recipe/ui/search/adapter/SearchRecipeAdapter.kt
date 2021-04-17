package com.devdd.recipe.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ItemViewSearchRecipeBinding
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.ui.search.SearchRecipeViewModel
import com.devdd.recipe.utils.extensions.bindWithLayout

class SearchRecipeAdapter(private val viewModel: SearchRecipeViewModel) :
    ListAdapter<RecipeViewState, SearchRecipeAdapter.SearchRecipeViewHolder>(
        RecipeDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecipeViewHolder {
        return SearchRecipeViewHolder.getInstance(parent, viewModel)
    }

    override fun onBindViewHolder(holder: SearchRecipeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class SearchRecipeViewHolder private constructor(
        private val binding: ItemViewSearchRecipeBinding,
        private val viewModel: SearchRecipeViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = viewModel
        }

        fun bind(item: RecipeViewState) {
            binding.recipe = item
            binding.executePendingBindings()
        }

        companion object {
            fun getInstance(
                parent: ViewGroup,
                viewModel: SearchRecipeViewModel
            ): SearchRecipeViewHolder {
                val binding =
                    bindWithLayout<ItemViewSearchRecipeBinding>(
                        R.layout.item_view_search_recipe,
                        parent
                    )
                return SearchRecipeViewHolder(binding, viewModel)
            }
        }
    }

    private object RecipeDiffItemCallback : DiffUtil.ItemCallback<RecipeViewState>() {
        override fun areItemsTheSame(oldItem: RecipeViewState, newItem: RecipeViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecipeViewState,
            newItem: RecipeViewState
        ): Boolean {
            return oldItem == newItem
        }
    }
}