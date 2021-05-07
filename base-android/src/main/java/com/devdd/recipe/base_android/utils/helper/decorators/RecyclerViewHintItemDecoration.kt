package com.devdd.recipe.base_android.utils.helper.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.utils.extensions.isFirstItem
import com.devdd.recipe.utils.extensions.isLastItem

class RecyclerViewHintItemDecoration : RecyclerView.ItemDecoration {

    private val mInterItemsGap: Int
    private val mNetOneSidedGap: Int

    private val itemWidth: Int

    constructor(context: Context, @Px itemWidth: Int, itemPeekingPercent: Float = .035f)
            : this(context.resources.displayMetrics.widthPixels, itemWidth, itemPeekingPercent)

    constructor(@Px totalWidth: Int, @Px itemWidth: Int, itemPeekingPercent: Float = .035f) {
        val cardPeekingWidth = (itemWidth * itemPeekingPercent + .5f).toInt()
        this.itemWidth = itemWidth
        mInterItemsGap = (totalWidth - itemWidth) / 2
        mNetOneSidedGap = mInterItemsGap / 2 - cardPeekingWidth
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = recyclerView.getChildAdapterPosition(view)
        val isFirstItem = recyclerView.isFirstItem(index)
        val isLastItem = recyclerView.isLastItem(index)

        val leftInset = if (isFirstItem) mInterItemsGap / 2 else mNetOneSidedGap / 2
        val rightInset = if (isLastItem) mInterItemsGap / 2 else mNetOneSidedGap / 2
        outRect.set(leftInset, 0, rightInset, 0)
    }
}
