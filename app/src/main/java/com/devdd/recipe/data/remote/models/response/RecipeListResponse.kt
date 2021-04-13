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
        @SerializedName("title_hi")
        var titleHi: String? = null,

        @Expose
        @SerializedName("author_name")
        var authorName: String? = null,

        @Expose
        @SerializedName("description")
        var description: String? = null,

        @Expose
        @SerializedName("description_hi")
        var descriptionHi: String? = null,

        @Expose
        @SerializedName("image_url")
        var imageUrl: String? = null,

        @Expose
        @SerializedName("total_time")
        var totalTime: String? = null,

        @Expose
        @SerializedName("preparing_time")
        var preparingTime: String? = null,

        @Expose
        @SerializedName("cooking_time")
        var cookingTime: String? = null,

        @Expose
        @SerializedName("category_name")
        var categoryName: String? = null,

        @Expose
        @SerializedName("ingrediants")
        var ingredients: List<String>? = null,

        @Expose
        @SerializedName("ingrediants_hi")
        var ingredientsHi: List<String>? = null,

        @Expose
        @SerializedName("saved")
        var saved: Boolean? = null
    )
}