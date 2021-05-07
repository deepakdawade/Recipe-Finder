package com.devdd.recipe.ui.utils.extensions

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.utils.helper.decorators.HorizontalMarginItemDecoration
import kotlin.math.abs


fun ViewPager2.setPageHint() {
    // You need to retain one page on each side so that the next and previous items are visible
    offscreenPageLimit = 1

    // Add a PageTransformer that translates the next and previous items horizontally
    // towards the center of the screen, which makes them visible
    val nextItemVisiblePx = resources.getDimension(R.dimen.margin32dp)
    val currentItemHorizontalMarginPx =
        resources.getDimension(R.dimen.margin32dp)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        // Next line scales the item's height. You can remove it if you don't want this effect
        page.scaleY = 1 - 0.25f * abs(position)
        // If you want a fading effect uncomment the next line:
        // page.alpha = 0.25f + (1 - abs(position))
    }
    setPageTransformer(pageTransformer)

    // The ItemDecoration gives the current (centered) item horizontal margin so that
    // it doesn't occupy the whole screen width. Without it the items overlap
    val itemDecoration =
        HorizontalMarginItemDecoration(
            context,
            R.dimen.margin32dp)
    addItemDecoration(itemDecoration)
}