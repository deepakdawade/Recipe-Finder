package com.devdd.recipe.ui.utils.helper.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.utils.extensions.isFirstItem
import com.devdd.recipe.utils.extensions.isLastItem
import com.devdd.recipe.utils.extensions.getDisplaySize

class RecyclerViewItemCountDecoration(
    private val context: Context,
    private val itemCount: Int,
    @Px private val widthToExclude: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        view.layoutParams.width = (context.getDisplaySize().width - widthToExclude) / itemCount

        val index = recyclerView.getChildAdapterPosition(view)
        val isFirstItem = recyclerView.isFirstItem(index)
        val isLastItem = recyclerView.isLastItem(index)

        val leftInset = if (isFirstItem) view.layoutParams.width else view.marginLeft
        val rightInset = if (isLastItem) view.layoutParams.width else view.marginRight
        outRect.set(leftInset, 0, rightInset, 0)
    }
}
