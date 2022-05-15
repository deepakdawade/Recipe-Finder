package com.devdd.recipe.ui

import com.devdd.recipe.data.local.model.Recipe
import com.devdd.recipe.utils.DateFormatter

data class RecipeViewState(
    val entity: Recipe,
    val isEnglish: Boolean = true
) {
    val description get() = if (isEnglish) entity.description else entity.descriptionHi

    val title get() = if (isEnglish) entity.title else entity.titleHi

    val ingredients: String
        get() {
            val ingredients = if (isEnglish)
                entity.ingredients
            else entity.ingredientsHi
            val builder = StringBuilder()
            ingredients.forEach {
                builder
                    .append("\u25CF\t")
                    .append(it)
                    .append("\n")
            }
            return builder.toString()
        }

    val savedDate get() = DateFormatter.timeToDayMonthDayFormatter(entity.savedTime)


}