package com.devdd.recipe.data.db.entities

import com.devdd.recipe.domain.viewstate.RecipeViewState

fun Recipe.toRecipeViewState(): RecipeViewState {
    return RecipeViewState(
        id = id,
        title = title,
        description = description,
        authorName = authorName,
        imageUrl = imageUrl
    )
}