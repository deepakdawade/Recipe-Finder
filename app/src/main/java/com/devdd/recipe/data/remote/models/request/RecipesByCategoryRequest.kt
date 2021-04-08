package com.devdd.recipe.data.remote.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecipesByCategoryRequest(
    @Expose
    @SerializedName("id")
    val id: Int? = null
)