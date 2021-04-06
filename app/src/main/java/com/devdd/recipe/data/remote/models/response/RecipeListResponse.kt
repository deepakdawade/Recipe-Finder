package com.devdd.recipe.data.remote.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @Expose
    @SerializedName("recipes")
    var recipes: List<RecipeResponse>? = null,
    ) {
    data class RecipeResponse(

        @Expose
        @SerializedName("id")
        var id: Int? = null,

        @Expose
        @SerializedName("title")
        var title: String? = null,

        @Expose
        @SerializedName("author_name")
        var authorName: String? = null,

        @Expose
        @SerializedName("description")
        var description: String? = null,

        @Expose
        @SerializedName("image_url")
        var imageUrl: String? = null
    )
}