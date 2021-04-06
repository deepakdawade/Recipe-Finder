package com.devdd.recipe.data.remote.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    @SerializedName("created_at")
    var createdAt: String? = null,

    @Expose
    @SerializedName("updated_at")
    var updatedAt: String? = null,

    @Expose
    @SerializedName("avatar")
    var avatar: Avatar? = null
) {
    data class Avatar(
        @Expose
        @SerializedName("url")
        var url: String? = null,
    )
}