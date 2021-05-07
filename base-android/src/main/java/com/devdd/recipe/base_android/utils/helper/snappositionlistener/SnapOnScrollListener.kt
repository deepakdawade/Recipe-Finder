package com.devdd.recipe.utils.snappositionlistener

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.devdd.recipe.utils.extensions.getSnapPosition
import com.devdd.recipe.base_android.utils.helper.snappositionlistener.OnSnapPositionChangeListener

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    private val onSnapPositionChangeListener: OnSnapPositionChangeListener? = null,
    private val behavior: Behavior = Behavior.NOTIFY_ON_SCROLL
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = recyclerView.getSnapPosition(snapHelper)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged || snapPosition == 0) {
            onSnapPositionChangeListener?.onSnapPositionChange(snapPosition, this.snapPosition)
            this.snapPosition = snapPosition
        }
    }

}
