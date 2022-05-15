package com.devdd.recipe.ui

import com.devdd.recipe.data.local.model.Recipe
import com.devdd.recipe.utils.DateFormatter

data class RecipeViewState(
    val entity: Recipe
) {
    fun description(isEnglish: Boolean) =
        if (isEnglish) entity.description else entity.descriptionHi

    fun title(isEnglish: Boolean) =
        if (isEnglish) entity.title else entity.titleHi

    fun ingredients(isEnglish: Boolean): String {
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

    fun savedDate() = DateFormatter.timeToDayMonthDayFormatter(entity.savedTime)


}