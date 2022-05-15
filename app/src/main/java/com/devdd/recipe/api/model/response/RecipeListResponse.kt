package com.devdd.recipe.api.model.response

import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @SerializedName("recipes")
    val recipes: List<RecipeResponse>? = null,
) {
    data class RecipeResponse(

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("title_hi")
        val titleHi: String? = null,

        @SerializedName("author_name")
        val authorName: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("description_hi")
        val descriptionHi: String? = null,

        @SerializedName("image_url")
        val imageUrl: String? = null,

        @SerializedName("total_time")
        val totalTime: String? = null,

        @SerializedName("preparing_time")
        val preparingTime: String? = null,

        @SerializedName("cooking_time")
        val cookingTime: String? = null,

        @SerializedName("category_name")
        val categoryName: String? = null,

        @SerializedName("ingrediants")
        val ingredients: List<String>? = null,

        @SerializedName("ingrediants_hi")
        val ingredientsHi: List<String>? = null,

        @SerializedName("saved")
        val saved: Boolean? = null,

        @SerializedName("saved_time")
        val savedTime: Long? = null,

        @SerializedName("message")
        val message: String? = null,

        @SerializedName("message_hi")
        val messageHi: String? = null
    )
}