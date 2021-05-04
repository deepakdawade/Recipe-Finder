package com.devdd.recipe.domain.viewstate

import android.content.Context
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.utils.DateFormatter
import com.devdd.recipe.utils.localemanager.LocaleManagerUtils

data class RecipeViewState(
    val entity: Recipe
) {
    fun description(context: Context) =
        if (LocaleManagerUtils.isEnglishLocale(context)) entity.description else entity.descriptionHi

    fun title(context: Context) =
        if (LocaleManagerUtils.isEnglishLocale(context)) entity.title else entity.titleHi

    fun ingredients(context: Context): String {
        val ingredients = if (LocaleManagerUtils.isEnglishLocale(context))
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