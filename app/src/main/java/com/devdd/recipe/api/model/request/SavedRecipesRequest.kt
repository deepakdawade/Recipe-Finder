package com.devdd.recipe.api.model.request

import com.google.gson.annotations.SerializedName

data class SavedRecipesRequest(
    @SerializedName("guest_token")
    val guestToken: String
)