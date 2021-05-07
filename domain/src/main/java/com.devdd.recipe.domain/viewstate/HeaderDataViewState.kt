package com.devdd.recipe.domain.viewstate

import androidx.annotation.StringRes


/**
 * Created by @author Deepak Dawade on 4/17/2021 at 9:09 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
sealed class HeaderDataViewState() {
    data class Header(
        @StringRes
        val stringRes: Int? = null,
        val string: String = ""
    ) : HeaderDataViewState()

    data class Data<T>(val list: List<T>) : HeaderDataViewState()
}

