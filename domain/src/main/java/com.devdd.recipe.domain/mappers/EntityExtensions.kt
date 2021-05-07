package com.devdd.recipe.domain.mappers

import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.domain.viewstate.RecipeViewState

fun Recipe.toRecipeViewState(): RecipeViewState {
    return RecipeViewState(
        entity = this
    )
}
