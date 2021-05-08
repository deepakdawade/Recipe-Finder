package com.devdd.recipe.ui.utils.extensions

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build.VERSION_CODES.*
import android.text.TextUtils
import android.view.*
import android.view.View.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.airbnb.lottie.LottieDrawable
import com.devdd.recipe.base.result.onSuccess
import com.devdd.recipe.base_android.utils.extensions.isAtLeastVersion
import com.devdd.recipe.ui.utils.helper.CountDrawable
import com.devdd.recipe.ui.utils.extensions.loadLottieFromUrl
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.MutableStateFlow


val MaterialToolbar.navigationIconView: View?
    get() {
        //check if contentDescription previously was set
        val hadContentDescription = !TextUtils.isEmpty(navigationContentDescription)
        val contentDescription =
            if (hadContentDescription) navigationContentDescription else "navigationIcon"
        navigationContentDescription = contentDescription
        val potentialViews = arrayListOf<View>()
        //find the view based on it's content description, set programmatically or with android:contentDescription
        findViewsWithText(potentialViews, contentDescription, FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        //Clear content description if not previously present
        if (!hadContentDescription) {
            navigationContentDescription = null
        }
        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
        return potentialViews.firstOrNull()
    }

fun ProgressBar.animateProgress(progress: Int) {
    if (progress == this.progress) return
    // will update the "progress" propriety of progressBar until it reaches providedProgress
    val animation = ObjectAnimator.ofInt(this, "progress", progress)
    animation.duration = 500 // 0.5 second
    animation.interpolator = DecelerateInterpolator()
    animation.start()
}

/**
 * Pops an orphaned [View] over the specified [anchor] using a [PopupWindow]
 */
@SuppressLint("ClickableViewAccessibility")
fun View.popOver(
    anchor: View,
    gravity: Int = Gravity.START,
    adjuster: () -> Point = { Point(0, 0) },
    options: PopupWindow.() -> Unit = {}
) {
    require(!this.isAttachedToWindow) { "The View being attached must be an orphan" }
    PopupWindow(
        this.wrapAtAnchor(anchor, adjuster),
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
        true
    ).run {
        isOutsideTouchable = true
        contentView.setOnTouchListener { _, _ ->
            dismiss()
            true
        }
        options(this)
        showAtLocation(anchor, gravity, 0, 0)
    }
}

private fun View.wrapAtAnchor(anchor: View, adjuster: () -> Point): View =
    FrameLayout(anchor.context).apply {
        this@wrapAtAnchor.alignToAnchor(anchor, adjuster)
        addView(this@wrapAtAnchor)
    }

private fun View.alignToAnchor(anchor: View, adjuster: () -> Point) = intArrayOf(0, 0).run {
    anchor.getLocationInWindow(this)
    doOnLayout {
        val (offsetX, offsetY) = adjuster()
        val x = this[0].toFloat() + offsetX
        val y = this[1].toFloat() + offsetY
        translationX = x; translationY = y
    }
}

fun BottomNavigationView.menuItemView(itemId: Int): View? {
    return findViewById(menu.findItem(itemId).itemId)
}

fun MaterialToolbar.menuItemView(itemId: Int): ActionMenuItemView? {
    return findViewById(menu.findItem(itemId).itemId)
}

fun MaterialToolbar.setBadge(itemId: Int, layerId: Int, count: Int) {
    val menuItem: MenuItem? = menu?.findItem(itemId)
    val icon: LayerDrawable = (menuItem?.icon as? LayerDrawable) ?: return

    val badge: CountDrawable

    // Reuse drawable if possible
    val reuse = icon.findDrawableByLayerId(layerId)
    badge = when {
        reuse != null && reuse is CountDrawable -> reuse
        else -> CountDrawable(context)
    }

    badge.setCount(count.toString())
    icon.mutate()
    icon.setDrawableByLayerId(layerId, badge)
}

const val DRAWABLE_LEFT: Int = 0
const val DRAWABLE_TOP: Int = 1
const val DRAWABLE_RIGHT: Int = 2
const val DRAWABLE_BOTTOM: Int = 3

@IntDef(DRAWABLE_LEFT, DRAWABLE_TOP, DRAWABLE_RIGHT, DRAWABLE_BOTTOM)
annotation class DrawablePosition

@SuppressLint("ClickableViewAccessibility")
inline fun MaterialTextView.setDrawableClickListener(
    @DrawablePosition position: Int,
    crossinline onClick: () -> Unit
) {
    setOnTouchListener(OnTouchListener { _, event ->
        return@OnTouchListener if (event.action == MotionEvent.ACTION_UP) {
            val consumeEvent = when (position) {
                DRAWABLE_LEFT -> {
                    event.rawX <= left + compoundDrawables[position].bounds.width() + paddingStart
                }
                DRAWABLE_TOP -> {
                    event.rawY <= top + compoundDrawables[position].bounds.width() + paddingTop
                }
                DRAWABLE_RIGHT -> {
                    event.rawX >= right - compoundDrawables[position].bounds.width() - paddingEnd
                }
                DRAWABLE_BOTTOM -> {
                    event.rawY >= bottom - compoundDrawables[position].bounds.width() - paddingBottom
                }
                else -> false
            }
            if (consumeEvent) onClick()
            consumeEvent
        } else event.action == MotionEvent.ACTION_DOWN
    })
}

fun AppCompatImageView.animateAVD(@DrawableRes drawableRes: Int) {
    val avd = AnimatedVectorDrawableCompat.create(context, drawableRes)
    setImageDrawable(avd)
    avd?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) = avd.start()
    })
    avd?.start()
}

fun MaterialToolbar.animateAVDMenuItem(@DrawableRes drawableRes: Int, itemId: Int) {
    val avd = AnimatedVectorDrawableCompat.create(context, drawableRes)
    menu?.findItem(itemId)?.icon = avd
    avd?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) = avd.start()
    })
    avd?.start()
}


fun AppCompatImageView.animateAVD(loop: Boolean) {
    val avd = drawable as? AnimatedVectorDrawableCompat ?: drawable as? AnimatedVectorDrawable
    if (loop) {
        when (avd) {
            is AnimatedVectorDrawable -> {
                if (isAtLeastVersion(M)) {
                    avd.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) = avd.start()
                    })
                }
            }
            is AnimatedVectorDrawableCompat -> {
                avd.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) = avd.start()
                })
            }
        }
    }
    avd?.start()
}

fun MaterialToolbar.animateLottieMenuItem(url: String, itemId: Int, repeatable: Boolean = true) {
    context.loadLottieFromUrl(url) {
        it.onSuccess {
            val menuItem = menu?.findItem(itemId)
            val view = menuItemView(itemId)
            callback = menuItem?.icon?.callback
            enableMergePathsForKitKatAndAbove(true)
            if (repeatable) repeatCount = LottieDrawable.INFINITE
            scale = 0.045f
            addAnimatorUpdateListener { view?.invalidate() }
            menuItem?.icon = this
            playAnimation()
        }
    }
}

fun BottomNavigationView.hideToolTip() {
    val content: View = getChildAt(0)
    if (content is ViewGroup) {
        content.forEach {
            it.setOnLongClickListener {
                return@setOnLongClickListener true
            }
            // disable vibration also
            it.isHapticFeedbackEnabled = false
        }
    }
}

fun SearchView.watchQueryTextChangeListener(stateFlow: MutableStateFlow<String>) {

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            stateFlow.value = query.toString()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            stateFlow.value = newText
            return true
        }
    })
}
