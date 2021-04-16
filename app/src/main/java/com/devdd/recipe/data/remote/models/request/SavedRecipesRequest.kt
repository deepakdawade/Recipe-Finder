package com.devdd.recipe.data.remote.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by @author Deepak Dawade on 4/17/2021 at 12:02 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
data class SavedRecipesRequest(
    @SerializedName("guest_token")
    @Expose
    val guestToken: String

)
