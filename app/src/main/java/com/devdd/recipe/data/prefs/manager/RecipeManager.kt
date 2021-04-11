package com.devdd.recipe.data.prefs.manager

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

    val recipePreference: Flow<String>
        get() = preference.recipePreference.catch { emit("") }

    suspend fun isRecipeSelected(): Boolean = recipePreference.first().isNotBlank()

    suspend fun isVegetarian(): Boolean {
        val type = recipePreference.first()
        return type == VEG
    }

    suspend fun isNonVegetarian(): Boolean {
        val type = recipePreference.first()
        return type == NON_VEG
    }

    suspend fun isBothVegNonVeg(): Boolean {
        val type = recipePreference.first()
        return type == BOTH
    }

    companion object {
        const val VEG = "Veg"
        const val NON_VEG = "Non Veg"
        const val BOTH = "Both"
    }
}