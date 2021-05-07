package com.devdd.recipe.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GuestResponse(
    @SerializedName("guest_token")
    @Expose
    val guestToken: String? = null
)