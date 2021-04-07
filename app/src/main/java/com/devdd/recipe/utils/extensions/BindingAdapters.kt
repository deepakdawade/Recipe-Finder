package com.devdd.exampleapp.commonui.utils.extensions

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.textview.MaterialTextView
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.utils.DateFormatter.toServerTime
import com.devdd.recipe.utils.extensions.animateAVD
import com.devdd.recipe.utils.extensions.animateProgress


@BindingAdapter("textValue", "textDefault")
fun setTextElseDefault(view: MaterialTextView, value: String?, default: String) {
    view.text = if (value.isNullOrBlank()) default else value
}

@BindingAdapter("strikeThru", "strikeThruText")
fun strikeThru(view: MaterialTextView, strike: Boolean, textValue: String) {
    if (strike)
        view.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = textValue
        } else {
        view.apply {
            paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            text = textValue
        }
    }

}

@BindingAdapter("toggleVisibility")
fun View.toggleVisibility(shouldShow: Boolean) {
    isVisible = shouldShow
}

@BindingAdapter("stringVisibility")
fun View.stringVisibility(string: String?) {
    isVisible = !string.isNullOrBlank()
}



@BindingAdapter("textColorTint")
fun textColor(view: MaterialTextView, color: Int) {
    view.apply { setTextColor(ContextCompat.getColor(context, color)) }
}

@BindingAdapter("textValue")
fun textValue(view: MaterialTextView, text: Int) {
    view.apply {
        setText(view.resources.getString(text))
    }
}

@BindingAdapter("stringSrc", "args")
fun stringValue(view: MaterialTextView, stringSrc: Int, args: Any) {
    view.apply {
        text = context.getString(stringSrc, args)
    }
}

@BindingAdapter("imageTint")
fun tintColorName(view: AppCompatImageView, color: Int) {
    view.apply {
        imageTintList = ColorStateList.valueOf(color)
    }
}

@BindingAdapter("goneIfNull")
fun goneIfNullOrBlank(view: View, string: String?) {
    view.visibility = if (string.isNullOrBlank()) View.GONE else View.VISIBLE
}

@BindingAdapter("hideWithAnimationIfNullAndBlank")
fun hideWithAnimationIfNullAndBlank(view: View, string: String?) {
    val visibility = if (string.isNullOrBlank()) View.GONE else View.VISIBLE
    val constraintLayout = view.parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    view.visibility = visibility
}

@BindingAdapter("showWithAnimationIfNullAndBlank")
fun showWithAnimationIfNullAndBlank(view: View, string: String?) {
    val visibility = if (string.isNullOrBlank()) View.VISIBLE else View.GONE
    val constraintLayout = view.parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    view.visibility = visibility
}

@BindingAdapter("animateVisibility")
fun animateVisibility(view: View, visibility: Int) {
    val constraintLayout = view.parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    view.visibility = visibility
}

@BindingAdapter("stringId", "stringValue")
fun formatString(view: MaterialTextView, stringId: Int?, stringValue: String) {
    view.visibility = if (stringId == null) {
        view.text = ""
        View.GONE
    } else {
        view.context.apply {
            view.text = String.format(stringValue, getString(stringId))
        }
        View.VISIBLE
    }
}

@BindingAdapter("showOnData", "shouldShow", requireAll = false)
fun showOnData(view: View, response: String?, shouldShow: Boolean = true) {
    view.visibility = if (response.isNullOrBlank() && shouldShow) View.GONE else View.VISIBLE
}


@BindingAdapter("tintOnIncomplete", "tintOnComplete", "determinateProgress")
fun changeWhenComplete(
    view: ProgressBar,
    tintOnIncomplete: Int,
    tintOnComplete: Int,
    determinateProgress: Int?
) {
    view.animateProgress(determinateProgress ?: 0)
    if (determinateProgress ?: 0 >= 100) view.progressTintList =
        ColorStateList.valueOf(tintOnComplete)
    else view.progressTintList = ColorStateList.valueOf(tintOnIncomplete)
}

@BindingAdapter("hideGroupWhenLoading")
fun hideGroupWhenLoading(view: Group, isLoading: Boolean?) {
    val visibility: Int = view.visibility
    view.visibility = when (isLoading) {
        false, null -> visibility
        true -> View.GONE
    }
}

@BindingAdapter("goneUnless")
fun goneWhenDataLoaded(view: ProgressBar, response: Boolean?) {
    view.visibility = if (response == true) View.VISIBLE else View.GONE
}

@BindingAdapter("showWhileFetching")
fun showWhileFetchData(view: ProgressBar, response: List<*>?) {
    view.visibility = if (response == null) View.VISIBLE else View.GONE
}

@BindingAdapter("hideOnEmptyList")
fun showOnData(view: View, response: List<*>?) {
    view.visibility = if (response.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("isRefreshing")
fun setRefreshState(view: SwipeRefreshLayout, response: Boolean?) {
    view.isRefreshing = response == true
}

@BindingAdapter("hideEmptyScreen", "showEmptyScreen", "disableEmptyScreen", requireAll = false)
fun showEmptyScreen(
    view: View,
    isLoading: Boolean,
    data: List<*>?,
    disableEmptyScreen: Boolean = false
) {
    if (disableEmptyScreen) {
        view.visibility = View.GONE
        return
    }
    val visibility = if (isLoading || data.isNullOrEmpty().not()) View.GONE else View.VISIBLE
    if (view.visibility != visibility) view.post { view.visibility = visibility }
}

@BindingAdapter("materialStyleProgress")
fun materialStyleProgress(view: ProgressBar, colorResIds: IntArray) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 8f
    circularProgressDrawable.setColorSchemeColors(*colorResIds)
    view.indeterminateDrawable = circularProgressDrawable
}

@BindingAdapter("srcVectorRes")
fun setSrcVectorRes(view: AppCompatImageView, @DrawableRes drawable: Int) {
    view.setImageResource(drawable)
}

@BindingAdapter("srcVectorDrawable")
fun setSrcVectorDrawable(view: AppCompatImageView, drawable: Drawable) {
    view.setImageDrawable(drawable)
}

@BindingAdapter(
    "dataToCheck",
    "dataCheckType",
    "checkOptional",
    "disableWhileLoading",
    requireAll = false
)
fun isDataValid(
    view: MaterialButton,
    dataToCheck: String?,
    dataCheckType: String,
    optional: Boolean = false,
    isLoading: Boolean?
) {
    view.isEnabled = false
    if (isLoading == true) return
    dataToCheck?.let {
        val shouldEnable: Boolean = isValid(it, dataCheckType)
        if (view.isEnabled != shouldEnable) view.isEnabled = shouldEnable
    }
}

@BindingAdapter("onDataValid", "includeNetworkState", requireAll = false)
fun onDataValid(view: MaterialButton, onDataValid: Boolean?, includeNetworkState: Boolean?) {
    view.isEnabled = false
    if (includeNetworkState == true) return
    onDataValid?.let { if (view.isEnabled != it) view.isEnabled = it }
}

@BindingAdapter("onCountDownFinish")
fun onCountDownFinish(view: MaterialButton, dataToCheck: Long?) {
    view.isEnabled = false
    dataToCheck?.let {
        val shouldEnable: Boolean = it == 0L
        if (view.isEnabled != shouldEnable) view.isEnabled = shouldEnable
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCountDownTimer")
fun setCountDownTimer(view: MaterialTextView, remainingTime: Long?) {
    remainingTime?.let {
        val secs: Int = it.toServerTime()
        val min: Int = secs / 60
        val secsText: String = (if (secs < 10) "0" else "") + secs
        val minText: String = (if (min < 10) "0" else "") + min
        view.text = "${minText}:${secsText}"
    }
}

@BindingAdapter(
    "imeAction",
    "imeActionId",
    "onValidInput",
    "inputToCheck",
    "inputCheckType",
    "isInputOptional",
    "skipValidation",
    requireAll = false
)
fun setImeActions(
    view: AppCompatEditText,
    imeAction: MutableLiveData<Event<Unit>>,
    imeActionId: Int,
    onValidInput: LiveData<Boolean>?,
    inputToCheck: LiveData<String>?,
    inputCheckType: String?,
    isInputOptional: Boolean = false,
    skipValidation: Boolean = false
) {
    view.imeOptions = imeActionId
//    if (hideKeyboardWhileLoading?.value == true) {
//        val imm: InputMethodManager? = view.context.getSystemService()
//        if (imm != null && imm.isActive(view)) {
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
    view.setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            imeActionId -> {
                val shouldPerformAction = inputToCheck?.let {
                    isValid(it.value ?: "", inputCheckType ?: "")
                } ?: onValidInput?.value == true

                (shouldPerformAction || skipValidation).also {
                    if (it) imeAction.value = Event(Unit)
                }
            }
            else -> false
        }
    }
}

@SuppressLint("WrongConstant")
@BindingAdapter("editable", "setEndIconMode")
fun disableEditText(
    view: TextInputLayout,
    enable: Boolean?,
    @TextInputLayout.EndIconMode endIconMode: Int
) {
    enable?.let {
        view.endIconMode = if (it) endIconMode else END_ICON_NONE
        view.editText?.isEnabled = it
        view.editText?.isFocusable = it
    }
}

@BindingAdapter("compareErrorMsg", "compareWith")
fun isMatching(view: TextInputLayout, errorMessage: String, compareWith: String?) {
    view.isErrorEnabled = true
    compareWith.let {
        if (view.editText?.text?.toString() != it ?: "") {
            view.error = errorMessage
        } else {
            view.error = null
        }
    }
    view.editText?.doOnTextChanged { text, _, _, _ ->
        if (text.toString() != compareWith) {
            view.error = errorMessage
        } else {
            view.error = null
        }
    }
}

@BindingAdapter("checkType", "checkErrorMsg", "fieldOptional", requireAll = false)
fun isValidName(
    view: TextInputLayout,
    type: String,
    errorMessage: String,
    optional: Boolean = false
) {
    view.isErrorEnabled = true
    view.editText?.doOnTextChanged { text, _, _, _ ->
        if (isValid(text.toString(), type).not()) view.error = errorMessage
        else {
            view.isErrorEnabled = false
            view.error = null
        }
    }
}

@BindingAdapter("spannedText")
fun spannedText(view: MaterialTextView, htmlFormattedText: String?) {
    view.apply {
        val spannedString = if (htmlFormattedText.isNullOrBlank()) htmlFormattedText
        else HtmlCompat.fromHtml(htmlFormattedText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        text = spannedString
    }
}

@BindingAdapter("android:textSize")
fun bindTextSize(textView: TextView, size: Int) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
}

@BindingAdapter("drawableTop")
fun drawableTop(view: MaterialTextView, drawable: Int) {
    view.apply {
        setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }
}

@BindingAdapter("avdLoop")
fun animateAVD(view: AppCompatImageView, loop: Boolean) {
    view.animateAVD(loop)
}


private fun isValid(text: String, type: String) = when (type) {
    DIGIT_ONLY -> text.isBlank()
    else -> false
}

private const val DIGIT_ONLY = "digitOnly"
