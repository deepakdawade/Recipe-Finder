package com.devdd.recipe.data.db

import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.domain.viewstate.RecipeViewState

fun Recipe.toRecipeViewState(isEnglishLocale: Boolean): RecipeViewState {
    return RecipeViewState(
        id = id,
        title = if (isEnglishLocale) title else titleHi,
        description = if (isEnglishLocale) description else descriptionHi,
        authorName = authorName,
        imageUrl = imageUrl,
        cookingTime = cookingTime,
        preparingTime = preparingTime,
        totalTime = totalTime,
        ingredients = if (isEnglishLocale) ingredients else ingredientsHi,
        saved = saved,
        savedTime = savedTime
    )
}
