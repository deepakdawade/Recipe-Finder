package com.devdd.recipe.mapper

import com.devdd.recipe.api.model.response.RecipeListResponse
import com.devdd.recipe.data.local.model.Recipe
import com.devdd.recipe.utils.Mapper
import javax.inject.Inject

class RecipeResponseToRecipeEntity @Inject constructor(
) : Mapper<RecipeListResponse.RecipeResponse, Recipe> {
    override suspend fun map(from: RecipeListResponse.RecipeResponse): Recipe {
        return from.toRecipeEntity()
    }

    private fun RecipeListResponse.RecipeResponse.toRecipeEntity(): Recipe {
        return Recipe(
            title = title.orEmpty(),
            titleHi = titleHi.orEmpty(),
            authorName = authorName.orEmpty(),
            description = description.orEmpty(),
            descriptionHi = descriptionHi.orEmpty(),
            id = id ?: 0,
            imageUrl = imageUrl.orEmpty(),
            categoryName = categoryName.orEmpty(),
            preparingTime = preparingTime.orEmpty(),
            totalTime = totalTime.orEmpty() ?: "",
            cookingTime = cookingTime.orEmpty(),
            ingredients = ingredients.orEmpty(),
            ingredientsHi = ingredientsHi.orEmpty(),
            saved = saved ?: false,
            savedTime = savedTime ?: 0,
            message = message.orEmpty(),
            messageHi = messageHi.orEmpty()
        )
    }
}