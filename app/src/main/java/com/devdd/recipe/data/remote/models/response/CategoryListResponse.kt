package com.devdd.recipe.data.remote.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryListResponse(
    @Expose
    @SerializedName("categories")
    val categories: List<CategoryResponse>? = null
) {
    data class CategoryResponse(
        @Expose
        @SerializedName("id")
        val id: Int? = null,
        @Expose
        @SerializedName("name")
        val name: String? = null,
        @Expose
        @SerializedName("description")
        val description: String? = null,
        @Expose
        @SerializedName("image_url")
        val imageUrl: String? = null
    )
}