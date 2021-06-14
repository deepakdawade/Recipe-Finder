package com.devdd.recipe.domain.viewstate

import com.devdd.recipe.data.models.entity.Recipe

fun Recipe.toRecipeViewState(): RecipeViewState {
    return RecipeViewState(
        entity = this
    )
}
