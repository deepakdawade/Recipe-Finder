package com.devdd.recipe.domain.mappers

import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.remote.models.response.RecipeListResponse.RecipeResponse
import javax.inject.Inject

class RecipeResponseToRecipeEntity @Inject constructor(
) : Mapper<RecipeResponse, Recipe> {
    override suspend fun map(from: RecipeResponse): Recipe {
        return from.toRecipeEntity()
    }

    private fun RecipeResponse.toRecipeEntity(): Recipe {
        return Recipe(
            title = title ?: "",
            titleHi = titleHi ?: "",
            authorName = authorName ?: "",
            description = description ?: "",
            descriptionHi = descriptionHi ?: "",
            id = id ?: 0,
            imageUrl = imageUrl ?: "",
            categoryName = categoryName ?: "",
            preparingTime = preparingTime?:"",
            totalTime = totalTime?:"",
            cookingTime = cookingTime?:""
        )
    }
}