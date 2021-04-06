package com.devdd.recipe.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ItemViewRecipeBinding
import com.devdd.recipe.ui.home.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.bindWithLayout


/**
 * Created by @author Deepak Dawade on 4/6/2021 at 10:08 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class RecipeAdapter() :
    ListAdapter<RecipeViewState, RecipeAdapter.RecipeViewHolder>(
        RecipeViewStateDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class RecipeViewHolder private constructor(private val binding: ItemViewRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeViewState) {
            binding.recipe = item
            binding.executePendingBindings()
        }

        companion object {
            fun getInstance(parent: ViewGroup): RecipeViewHolder {
                val binding =
                    bindWithLayout<ItemViewRecipeBinding>(R.layout.item_view_recipe, parent)
                return RecipeViewHolder(binding)
            }
        }
    }

    private object RecipeViewStateDiffItemCallback : DiffUtil.ItemCallback<RecipeViewState>() {
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