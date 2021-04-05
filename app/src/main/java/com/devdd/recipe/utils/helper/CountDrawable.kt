package com.devdd.recipe.utils.helper

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.devdd.recipe.R
import com.devdd.recipe.utils.extensions.themedColor
import com.devdd.recipe.utils.extensions.toDimensions
import kotlin.math.max


class CountDrawable(context: Context) : Drawable() {
    private val mBadgePaint: Paint = Paint()
    private val mTextPaint: Paint
    private val mTxtRect: Rect = Rect()
    private var mCount = ""
    private var mWillDraw = false

    override fun draw(canvas: Canvas) {
        if (!mWillDraw) {
            return
        }
        val bounds: Rect = bounds
        val width: Float = (bounds.right - bounds.left).toFloat()
        val height: Float = (bounds.bottom - bounds.top).toFloat()

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */
        val radius = max(width, height) / 2 / 2
        val centerX = width - radius - 1 + 5
        val centerY = radius - 5
        if (mCount.length <= 2) {
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (radius + 5.5).toFloat(), mBadgePaint)
        } else {
            canvas.drawCircle(centerX, centerY, (radius + 6.5).toFloat(), mBadgePaint)
        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)
        val textHeight: Float = (mTxtRect.bottom - mTxtRect.top).toFloat()
        val textY = centerY + textHeight / 2f
        if (mCount.length > 2) canvas.drawText(
            "99+",
            centerX,
            textY,
            mTextPaint
        ) else canvas.drawText(mCount, centerX, textY, mTextPaint)
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    fun setCount(count: String) {
        mCount = count

        // Only draw a badge if there are notifications.
        mWillDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        // do nothing
    }

    override fun setColorFilter(cf: ColorFilter?) {
        // do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    init {
        mBadgePaint.color = context.themedColor(R.attr.colorError)
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
        mTextPaint = Paint()
        mTextPaint.color = Color.WHITE
        mTextPaint.typeface = Typeface.DEFAULT
        mTextPaint.textSize = context.toDimensions(10f)
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
    }
}