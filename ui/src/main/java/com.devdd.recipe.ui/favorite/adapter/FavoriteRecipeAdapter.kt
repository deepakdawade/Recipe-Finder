package com.devdd.recipe.ui.favorite.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.ui.R
import com.devdd.recipe.domain.viewstate.HeaderDataViewState
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.ui.databinding.ItemViewSavedRecipesBinding
import com.devdd.recipe.ui.databinding.ItemViewSectionHeaderBinding
import com.devdd.recipe.ui.favorite.FavoriteViewModel
import com.devdd.recipe.utils.extensions.bindWithLayout


/**
 * Created by @author Deepak Dawade on 4/17/2021 at 10:37 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class FavoriteRecipeAdapter(private val viewModel: FavoriteViewModel) :
    ListAdapter<HeaderDataViewState, FavoriteRecipeAdapter.FavoriteRecipeViewHolder>(
        FavoriteDiffItemCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        return when (viewType) {
            R.layout.item_view_section_header -> FavoriteRecipeViewHolder.HeaderViewHolder.getInstance(
                parent
            )
            R.layout.item_view_saved_recipes -> FavoriteRecipeViewHolder.DataViewHolder.getInstance(
                parent,
                viewModel
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is HeaderDataViewState.Header -> R.layout.item_view_section_header
            is HeaderDataViewState.Data<*> -> R.layout.item_view_saved_recipes
            else -> super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is FavoriteRecipeViewHolder.DataViewHolder -> holder.bind(item as HeaderDataViewState.Data<RecipeViewState>)
            is FavoriteRecipeViewHolder.HeaderViewHolder -> holder.bind(item as HeaderDataViewState.Header)
        }
    }

    sealed class FavoriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class HeaderViewHolder private constructor(private val binding: ItemViewSectionHeaderBinding) :
            FavoriteRecipeViewHolder(binding.root) {
            fun bind(item: HeaderDataViewState.Header) {
                binding.header = item
                binding.executePendingBindings()
            }

            companion object {
                fun getInstance(
                    parent: ViewGroup,
                ): HeaderViewHolder {
                    val binding: ItemViewSectionHeaderBinding =
                        bindWithLayout(
                            R.layout.item_view_section_header,
                            parent
                        )
                    return HeaderViewHolder(binding)
                }
            }
        }

        class DataViewHolder private constructor(
            private val binding: ItemViewSavedRecipesBinding,
            private val viewModel: FavoriteViewModel
        ) :
            FavoriteRecipeViewHolder(binding.root) {
            fun bind(item: HeaderDataViewState.Data<RecipeViewState>) {
                binding.savedRecipeItemViewRecycler.adapter = SavedRecipeAdapter(viewModel).apply {
                    submitList(item.list)
                }
                binding.executePendingBindings()
            }

            companion object {
                fun getInstance(
                    parent: ViewGroup,
                    viewModel: FavoriteViewModel
                ): DataViewHolder {
                    val binding: ItemViewSavedRecipesBinding =
                        bindWithLayout(
                            R.layout.item_view_saved_recipes,
                            parent
                        )
                    return DataViewHolder(binding, viewModel)
                }
            }
        }
    }

    private object FavoriteDiffItemCallback : DiffUtil.ItemCallback<HeaderDataViewState>() {
        override fun areItemsTheSame(
            oldItem: HeaderDataViewState,
            newItem: HeaderDataViewState
        ): Boolean {
            return when {
                oldItem is HeaderDataViewState.Header && newItem is HeaderDataViewState.Header -> oldItem.string == newItem.string
                oldItem is HeaderDataViewState.Data<*> && newItem is HeaderDataViewState.Data<*> -> oldItem.list == newItem.list
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: HeaderDataViewState,
            newItem: HeaderDataViewState
        ): Boolean {
            return when {
                oldItem is HeaderDataViewState.Header && newItem is HeaderDataViewState.Header -> oldItem == newItem
                oldItem is HeaderDataViewState.Data<*> && newItem is HeaderDataViewState.Data<*> -> oldItem == newItem
                else -> false
            }
        }
    }
}