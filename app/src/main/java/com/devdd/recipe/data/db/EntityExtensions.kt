package com.devdd.recipe.data.db

import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.domain.viewstate.CategoryViewState
import com.devdd.recipe.domain.viewstate.RecipeViewState

fun Recipe.toRecipeViewState(isEnglish: Boolean): RecipeViewState {
    return RecipeViewState(
        id = id,
        title = if (isEnglish) title else titleHi,
        description = if (isEnglish) description else descriptionHi,
        authorName = authorName,
        imageUrl = imageUrl
    )
}

fun Category.toCategoryViewState(isEnglish: Boolean): CategoryViewState {
    return CategoryViewState(
        id = id,
        description = if (isEnglish) description else descriptionHi,
        name = if (isEnglish) name else nameHi,
        imageUrl = imageUrl
    )
}