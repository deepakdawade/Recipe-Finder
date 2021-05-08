package com.devdd.recipe.feature_profile.ui.profile.devoption.selectoption.adapter

import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.feature_profile.R
import com.devdd.recipe.feature_profile.databinding.ItemViewSelectOptionBinding
import com.devdd.recipe.ui.utils.extensions.bindWithLayout

class SelectOptionAdapter(private val listener: OnOptionSelectListener) :
    ListAdapter<DeveloperOption, SelectOptionAdapter.SelectOptionViewHolder>(
        SelectOptionDiffCallback
    ) {


    fun interface OnOptionSelectListener {
        fun onOptionClick(direction: NavDirections)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectOptionViewHolder {
        return SelectOptionViewHolder.getViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: SelectOptionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SelectOptionViewHolder(
        private val binding: ItemViewSelectOptionBinding,
        private val listener: OnOptionSelectListener
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getViewHolder(
                parent: ViewGroup,
                listener: OnOptionSelectListener
            ): SelectOptionViewHolder {
                val binding: ItemViewSelectOptionBinding = bindWithLayout(
                    layoutId = R.layout.item_view_select_option,
                    parent = parent
                )
                return SelectOptionViewHolder(binding, listener)
            }
        }

        fun bind(option: DeveloperOption) {
            binding.option = option
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    object SelectOptionDiffCallback : DiffUtil.ItemCallback<DeveloperOption>() {
        override fun areItemsTheSame(oldItem: DeveloperOption, newItem: DeveloperOption): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: DeveloperOption,
            newItem: DeveloperOption
        ): Boolean {
            return oldItem == newItem
        }
    }
}
