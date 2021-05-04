package com.devdd.recipe.utils.bindingadapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.devdd.recipe.utils.DateFormatter.toServerTime
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("spannedText")
fun MaterialTextView.spannedText(htmlFormattedText: String?) {
    val spannedString = if (htmlFormattedText.isNullOrBlank()) htmlFormattedText
    else HtmlCompat.fromHtml(htmlFormattedText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    text = spannedString
}

@BindingAdapter("drawableTop")
fun MaterialTextView.drawableTop(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCountDownTimer")
fun MaterialTextView.setCountDownTimer(remainingTime: Long?) {
    remainingTime?.let {
        val secs: Int = it.toServerTime()
        val min: Int = secs / 60
        val secsText: String = (if (secs < 10) "0" else "") + secs
        val minText: String = (if (min < 10) "0" else "") + min
        text = "${minText}:${secsText}"
    }
}

@BindingAdapter("textValue", "textDefault")
fun MaterialTextView.setTextElseDefault(value: String?, default: String) {
    text = if (value.isNullOrBlank()) default else value
}

@BindingAdapter("strikeThru", "strikeThruText")
fun MaterialTextView.strikeThru(strike: Boolean, textValue: String) {
    paintFlags = if (strike)
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    else
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    text = textValue
}
