package com.devdd.recipe.ui.profile.devoption.selectoption.adapter

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavDirections


/**
 * Created by @author Deepak Dawade on 4/14/2021 at 10:08 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
data class DeveloperOption(
    @StringRes val name: Int,
    @ColorRes val bgColor: Int,
    @DrawableRes val icon:Int,
    val direction:NavDirections?
)