package com.devdd.recipe.utils.bindingadapter

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("checkType", "checkErrorMsg", requireAll = false)
fun TextInputLayout.isValidName(
    type: String,
    errorMessage: String
) {
    isErrorEnabled = true
    editText?.doOnTextChanged { text, _, _, _ ->
        if (isValid(text.toString(), type).not()) error = errorMessage
        else {
            isErrorEnabled = false
            error = null
        }
    }
}

@BindingAdapter("editable", "setEndIconMode")
fun TextInputLayout.disableEditText(
    enable: Boolean?,
    @TextInputLayout.EndIconMode endIconMode: Int
) {
    enable?.let {
        this.endIconMode = if (it) endIconMode else TextInputLayout.END_ICON_NONE
        editText?.isEnabled = it
        editText?.isFocusable = it
    }
}

@BindingAdapter("compareErrorMsg", "compareWith")
fun TextInputLayout.isMatching(errorMessage: String, compareWith: String?) {
    isErrorEnabled = true
    compareWith.let {
        error = if (editText?.text?.toString() != it ?: "")
            errorMessage
        else null
    }
    editText?.doOnTextChanged { text, _, _, _ ->
        error = if (text.toString() != compareWith)
            errorMessage
        else null
    }
}
