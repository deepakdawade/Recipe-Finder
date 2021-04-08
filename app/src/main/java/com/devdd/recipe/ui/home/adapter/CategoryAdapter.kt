package com.devdd.recipe.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.R
import com.devdd.recipe.domain.viewstate.CategoryViewState
import com.devdd.recipe.databinding.ItemViewCategoryBinding
import com.devdd.recipe.utils.extensions.bindWithLayout


class CategoryAdapter(private val listener:CategoryClickListener) :
    ListAdapter<CategoryViewState, CategoryAdapter.CategoryViewHolder>(
        CategoryDiffItemCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.getInstance(parent,listener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class CategoryViewHolder private constructor(
        private val binding: ItemViewCategoryBinding,
        clickListener: CategoryClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listener = clickListener
        }
        fun bind(item: CategoryViewState) {
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun getInstance(parent: ViewGroup, categoryClickListener: CategoryClickListener): CategoryViewHolder {
                val binding =
                    bindWithLayout<ItemViewCategoryBinding>(R.layout.item_view_category, parent)
                return CategoryViewHolder(binding,categoryClickListener)
            }
        }
    }

    private object CategoryDiffItemCallback : DiffUtil.ItemCallback<CategoryViewState>() {
        override fun areItemsTheSame(
            oldItem: CategoryViewState,
            newItem: CategoryViewState
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryViewState,
            newItem: CategoryViewState
        ): Boolean {
            return oldItem == newItem
        }
    }

    fun interface CategoryClickListener {
        fun onCategoryClick(viewState: CategoryViewState)
    }
}