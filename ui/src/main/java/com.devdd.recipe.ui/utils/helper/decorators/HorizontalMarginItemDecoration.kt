package com.devdd.recipe.ui.utils.helper.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.devdd.recipe.ui.utils.extensions.isFirstItem
import com.devdd.recipe.ui.utils.extensions.isLastItem

/**
 * Adds margin to the left and right sides of the RecyclerView item.
 * Adapted from https://stackoverflow.com/a/27664023/4034572
 * @param horizontalMarginInDp the margin resource, in dp.
 */
class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State
    ) {
        val index = recyclerView.getChildAdapterPosition(view)
        val leftInset =
            if (recyclerView.isFirstItem(index)) horizontalMarginInPx / 2 else horizontalMarginInPx
        val rightInset =
            if (recyclerView.isLastItem(index)) horizontalMarginInPx / 2 else horizontalMarginInPx
        outRect.set(leftInset, 0, rightInset, 0)
    }

}