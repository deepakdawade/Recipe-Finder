package com.devdd.recipe.data.remote.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by @author Deepak Dawade on 4/13/2021 at 11:28 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
data class FetchRecipeRequest(
    @SerializedName("guest_token")
    @Expose
    val guestToken: String = ""
)