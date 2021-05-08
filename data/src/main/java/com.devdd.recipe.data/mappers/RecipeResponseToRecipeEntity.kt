package com.devdd.recipe.data.mappers

import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.models.response.RecipeListResponse
import javax.inject.Inject

class RecipeResponseToRecipeEntity @Inject constructor(
) : Mapper<RecipeListResponse.RecipeResponse, Recipe> {
    override suspend fun map(from: RecipeListResponse.RecipeResponse): Recipe {
        return from.toRecipeEntity()
    }

    private fun RecipeListResponse.RecipeResponse.toRecipeEntity(): Recipe {
        return Recipe(
            title = title ?: "",
            titleHi = titleHi ?: "",
            authorName = authorName ?: "",
            description = description ?: "",
            descriptionHi = descriptionHi ?: "",
            id = id ?: 0,
            imageUrl = imageUrl ?: "",
            categoryName = categoryName ?: "",
            preparingTime = preparingTime ?: "",
            totalTime = totalTime ?: "",
            cookingTime = cookingTime ?: "",
            ingredients = ingredients ?: emptyList(),
            ingredientsHi = ingredientsHi ?: emptyList(),
            saved = saved ?: false,
            savedTime = savedTime ?: 0
        )
    }
}