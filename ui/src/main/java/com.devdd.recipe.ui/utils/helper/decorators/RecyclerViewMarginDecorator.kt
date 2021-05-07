package com.devdd.exampleapp.commonui.utils.helpers.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.utils.extensions.isFirstItem
import com.devdd.recipe.utils.extensions.isLastItem

class RecyclerViewMarginDecorator(
    private val marginTop: Int = NO_MARGIN_APPLIED,
    private val marginStart: Int = NO_MARGIN_APPLIED,
    private val marginEnd: Int = NO_MARGIN_APPLIED,
    private val marginBottom: Int = NO_MARGIN_APPLIED
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = recyclerView.getChildAdapterPosition(view)
        val isFirstItem = recyclerView.isFirstItem(index)
        val isLastItem = recyclerView.isLastItem(index)


        outRect.set(
            insetOrDefault(marginStart, isFirstItem), insetOrDefault(marginTop, isFirstItem),
            insetOrDefault(marginEnd, isLastItem), insetOrDefault(marginBottom, isLastItem)
        )
    }

    private fun insetOrDefault(margin: Int, shouldApply: Boolean) =
        if (margin != NO_MARGIN_APPLIED && shouldApply) margin else 0

    companion object {
        private const val NO_MARGIN_APPLIED = -1
    }
}
