package com.devdd.recipe.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ItemViewHomeRecipeBinding
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.ui.home.HomeViewModel
import com.devdd.recipe.utils.extensions.bindWithLayout

class RecipeAdapter(private val viewModel: HomeViewModel) :
    ListAdapter<RecipeViewState, RecipeAdapter.RecipeViewHolder>(
        RecipeDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.getInstance(parent, viewModel)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class RecipeViewHolder private constructor(
        private val binding: ItemViewHomeRecipeBinding,
        private val viewModel: HomeViewModel
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
                viewModel: HomeViewModel
            ): RecipeViewHolder {
                val binding: ItemViewHomeRecipeBinding =
                    bindWithLayout(
                        R.layout.item_view_home_recipe,
                        parent
                    )
                return RecipeViewHolder(binding, viewModel)
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