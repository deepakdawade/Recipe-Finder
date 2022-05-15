package com.devdd.recipe.api.model.response

import com.google.gson.annotations.SerializedName

data class GuestResponse(
    @SerializedName("guest_token")
    val guestToken: String? = null
)