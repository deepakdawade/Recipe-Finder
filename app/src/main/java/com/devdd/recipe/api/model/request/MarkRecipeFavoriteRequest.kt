package com.devdd.recipe.api.model.request

import com.google.gson.annotations.SerializedName

data class MarkRecipeFavoriteRequest(
    @SerializedName("guest_token")
    val guestToken: String,

    @SerializedName("recipe_id")
    val recipeId: Int,

    @SerializedName("saved")
    val saved: Boolean
)