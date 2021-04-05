package com.devdd.recipe.utils.extensions

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.devdd.recipe.utils.snappositionlistener.SnapOnScrollListener
import com.devdd.recipe.utils.helper.snappositionlistener.OnSnapPositionChangeListener
import kotlin.math.abs
import kotlin.math.max

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollListener(
            snapHelper,
            onSnapPositionChangeListener,
            behavior
        )
    addOnScrollListener(snapOnScrollListener)
}

@Suppress("unused")
fun RecyclerView.isFirstItem(index: Int): Boolean = index == 0
fun RecyclerView.isLastItem(index: Int): Boolean = index == adapter!!.itemCount - 1

fun RecyclerView.getSnapPosition(snapHelper: SnapHelper): Int {
    val layoutManager = layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.snapToPosition(snapHelper: SnapHelper, position: Int) {
    val layoutManager = layoutManager ?: return
    val itemCount = adapter?.itemCount ?: return
    val currentPosition = getSnapPosition(snapHelper)

    if (position < 0) return
    if (position > itemCount - 1) return
    if (position == currentPosition) return

    val smoothScroller: RecyclerView.SmoothScroller? = createSnapScroller(snapHelper)
    post {
        if (smoothScroller != null) {
            smoothScroller.targetPosition = position
            layoutManager.startSmoothScroll(smoothScroller)
        }
    }
}

fun RecyclerView.snapIn(snapHelper: SnapHelper, snapDirection: SnapDirection) {
    val currentPosition = getSnapPosition(snapHelper)

    val targetPosition = when (snapDirection) {
        SnapDirection.START -> currentPosition - 1
        SnapDirection.END -> currentPosition + 1
    }
    snapToPosition(snapHelper, targetPosition)
}

fun RecyclerView.createSnapScroller(snapHelper: SnapHelper): LinearSmoothScroller? {
    val layoutManager = layoutManager ?: return null
    return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
        null
    } else object : LinearSmoothScroller(context) {
        override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
            val snapDistances: IntArray =
                snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView) ?: return
            val dx = snapDistances[0]
            val dy = snapDistances[1]
            val time = calculateTimeForDeceleration(max(abs(dx), abs(dy)))
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator)
            }
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 100f / displayMetrics.densityDpi
        }
    }
}

enum class SnapDirection {
    START,
    END
}