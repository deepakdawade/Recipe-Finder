package com.devdd.recipe.ui.favorite.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ItemViewSavedRecipeItemBinding
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.ui.favorite.FavoriteViewModel
import com.devdd.recipe.utils.extensions.bindWithLayout

class SavedRecipeAdapter(private val viewModel: FavoriteViewModel) :
    ListAdapter<RecipeViewState, SavedRecipeAdapter.SavedRecipeViewHolder>(
        RecipeDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        return SavedRecipeViewHolder.getInstance(parent, viewModel)
    }

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class SavedRecipeViewHolder private constructor(
        private val binding: ItemViewSavedRecipeItemBinding,
        private val viewModel: FavoriteViewModel
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
                viewModel: FavoriteViewModel
            ): SavedRecipeViewHolder {
                val binding =
                    bindWithLayout<ItemViewSavedRecipeItemBinding>(
                        R.layout.item_view_saved_recipe_item,
                        parent
                    )
                return SavedRecipeViewHolder(binding, viewModel)
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