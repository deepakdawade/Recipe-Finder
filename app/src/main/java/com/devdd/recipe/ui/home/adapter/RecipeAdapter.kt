package com.devdd.recipe.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ItemViewRecipeBinding
import com.devdd.recipe.data.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.bindWithLayout

class RecipeAdapter(private val recipeClickListener: RecipeClickListener) :
    ListAdapter<RecipeViewState, RecipeAdapter.RecipeViewHolder>(
        RecipeDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.getInstance(parent, recipeClickListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class RecipeViewHolder private constructor(
        private val binding: ItemViewRecipeBinding,
        private val recipeClickListener: RecipeClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeViewState) {
            binding.recipe = item
            binding.clickListener = recipeClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun getInstance(
                parent: ViewGroup,
                recipeClickListener: RecipeClickListener
            ): RecipeViewHolder {
                val binding =
                    bindWithLayout<ItemViewRecipeBinding>(R.layout.item_view_recipe, parent)
                return RecipeViewHolder(binding, recipeClickListener)
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

    fun interface RecipeClickListener {
        fun onRecipeClick(viewState: RecipeViewState)
    }
}