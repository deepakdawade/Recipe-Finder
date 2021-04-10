package com.devdd.recipe.data.prefs.manager

import com.devdd.recipe.constants.RecipePreference
import com.devdd.recipe.data.prefs.DataStorePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecipeManager @Inject constructor(
    private val preference: DataStorePreference
) {

    suspend fun updateRecipePref(pref: String) {
        preference.setRecipePreference(pref)
    }

    private val recipePreference: Flow<String>
        get() = preference.recipePreference.catch { emit("") }

    suspend fun isRecipeSelected(): Boolean = recipePreference.first().isNotBlank()

    suspend fun isVegetarian(): Boolean {
        val type = recipePreference.first()
        return type == RecipePreference.VEG
    }

    suspend fun isNonVegetarian(): Boolean {
        val type = recipePreference.first()
        return type == RecipePreference.NON_VEG
    }

    suspend fun isBothVegNonVeg(): Boolean {
        val type = recipePreference.first()
        return type == RecipePreference.BOTH
    }

}