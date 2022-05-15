package com.devdd.recipe.data.local.pref.manager

import com.devdd.recipe.data.local.pref.DataStorePreference
import com.devdd.recipe.utils.RecipePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeManager @Inject constructor(
    private val preference: DataStorePreference
) {

    suspend fun updateRecipePref(pref: String) {
        preference.setRecipePreference(pref)
    }

    val recipePreference: Flow<String> = preference.recipePreference.catch { emit("") }

    val preferenceSelected: Flow<Boolean> = recipePreference.map { it.isNotBlank() }

    fun isVegetarian(pref: String): Boolean = pref == RecipePreference.VEG

    fun isNonVegetarian(pref: String): Boolean = pref == RecipePreference.NON_VEG

    fun isBothVegNonVeg(pref: String): Boolean {
        return pref == RecipePreference.BOTH
    }
}