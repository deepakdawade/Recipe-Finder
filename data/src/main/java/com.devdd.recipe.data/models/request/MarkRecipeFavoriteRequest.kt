package com.devdd.recipe.data.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by @author Deepak Dawade on 4/16/2021 at 11:21 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
data class MarkRecipeFavoriteRequest(
    @SerializedName("guest_token")
    @Expose
    val guestToken: String,
    @SerializedName("recipe_id")
    @Expose
    val recipeId: Int,
    @SerializedName("saved")
    val saved: Boolean
)
