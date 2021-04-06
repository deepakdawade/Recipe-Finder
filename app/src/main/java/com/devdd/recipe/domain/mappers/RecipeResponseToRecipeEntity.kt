package com.devdd.recipe.domain.mappers

import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.mappers.Mapper
import com.devdd.recipe.data.remote.models.response.RecipeResponse
import javax.inject.Inject

class RecipeResponseToRecipeEntity @Inject constructor(
) : Mapper<RecipeResponse, Recipe> {
    override suspend fun map(from: RecipeResponse): Recipe {
        return from.toRecipeEntity()
    }

    private fun RecipeResponse.toRecipeEntity(): Recipe {
        return Recipe(
            title = title ?: "",
            authorName = authorName ?: "",
            description = description ?: "",
            id = id ?: 0,
            imageUrl = avatar?.url ?: ""
        )
    }
}