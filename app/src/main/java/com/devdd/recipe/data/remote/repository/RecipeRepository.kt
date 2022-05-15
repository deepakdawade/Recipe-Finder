package com.devdd.recipe.data.remote.repository

import com.devdd.recipe.api.model.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.api.model.request.SavedRecipesRequest
import com.devdd.recipe.data.local.dao.RecipeDao
import com.devdd.recipe.data.local.model.Recipe
import com.devdd.recipe.data.local.pref.manager.GuestManager
import com.devdd.recipe.data.local.pref.manager.LocaleManager
import com.devdd.recipe.data.local.pref.manager.RecipeManager
import com.devdd.recipe.data.remote.source.RecipeDataSource
import com.devdd.recipe.ui.RecipeViewState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface RecipeRepository {
    val selectedRecipePref: Flow<String>

    val selectedLocale: Flow<String>

    suspend fun getGuestToken(): String

    suspend fun fetchRecipes(guestToken: String)

    suspend fun setDeviceId(deviceId: String)

    suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest)

    suspend fun fetchSavedRecipes(request: SavedRecipesRequest)

    fun observeRecipeByPref(pref: String): Flow<List<RecipeViewState>>

    fun searchRecipes(query: String): Flow<List<RecipeViewState>>

    fun getRecipeById(id: Int): Flow<RecipeViewState>

    suspend fun setRecipePrefSelected(pref: String)

    suspend fun setAppLocaleSelected(locale: String)
}

class RecipeRepositoryImpl @Inject constructor(
    private val recipeManager: RecipeManager,
    private val guestManager: GuestManager,
    private val localeManager: LocaleManager,
    private val dataSource: RecipeDataSource,
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override val selectedRecipePref: Flow<String> = recipeManager.recipePreference
    override val selectedLocale: Flow<String> = localeManager.selectedLanguage
    override suspend fun getGuestToken(): String {
        val guestResponse = dataSource.fetchGuestToken()
        val token = guestResponse.guestToken ?: ""
        guestManager.updateGuestToken(token)
        return token
    }

    override suspend fun fetchRecipes(guestToken: String) {
        val recipes = dataSource.fetchRecipes(guestToken)
        val localRecipes = recipeDao.allRecipes().first()
        val insertIntoDB = recipes.isNotEmpty() && recipes != localRecipes
        if (insertIntoDB) recipeDao.insertRecipe(*recipes.toTypedArray())
    }

    override suspend fun setDeviceId(deviceId: String) {
        dataSource.setDeviceId(deviceId)
    }

    override suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest) {
        dataSource.markRecipeFavorite(request)
        val recipe = recipeDao.recipeById(request.recipeId).first()
        recipeDao.insertRecipe(recipe.copy(saved = request.saved))
    }

    override suspend fun fetchSavedRecipes(request: SavedRecipesRequest) {
        val recipes = dataSource.fetchSavedRecipes(request)
        val localRecipes = recipeDao.allRecipes().first()
        val insertIntoDB = recipes.isNotEmpty() && recipes != localRecipes
        if (insertIntoDB) {
            if (localRecipes.isNotEmpty())
                recipeDao.dropRecipes()
            recipeDao.insertRecipe(*recipes.toTypedArray())
        }
    }

    override fun observeRecipeByPref(pref: String): Flow<List<RecipeViewState>> {
        val recipeFlow = if (pref.isBlank() || recipeManager.isBothVegNonVeg(pref)) recipeDao.allRecipes()
            else recipeDao.recipesByPref(pref)
        return recipeFlow.mapRecipesToViewState()
    }

    override fun searchRecipes(query: String): Flow<List<RecipeViewState>> {
        val recipes =
            recipeDao.allRecipes().combine(recipeManager.recipePreference) { recipes, pref ->
                val result = if (recipeManager.isBothVegNonVeg(pref)) recipes
                else
                    recipes.filter { it.categoryName == pref }
                result.filter { recipe ->
                    recipe.title.contains(query, true) ||
                            recipe.titleHi.contains(query, true) ||
                            recipe.description.contains(query, true) ||
                            recipe.descriptionHi.contains(query, true) ||
                            recipe.authorName.contains(query, true) ||
                            recipe.ingredients.contains(query) ||
                            recipe.ingredientsHi.contains(query)
                }
            }
        return recipes.mapRecipesToViewState()
    }

    override fun getRecipeById(id: Int): Flow<RecipeViewState> {
        return recipeDao.recipeById(id).combine(localeManager.selectedLanguage) { recipe, locale ->
            RecipeViewState(recipe, localeManager.isEnglishLocale(locale))
        }
    }

    private fun Flow<List<Recipe>>.mapRecipesToViewState(): Flow<List<RecipeViewState>> {
        return this.combine(localeManager.selectedLanguage) { recipes, locale ->
            recipes.map {
                RecipeViewState(it, localeManager.isEnglishLocale(locale))
            }
        }
    }

    override suspend fun setRecipePrefSelected(pref: String) {
        recipeManager.updateRecipePref(pref)
    }

    override suspend fun setAppLocaleSelected(locale: String) {
        localeManager.updateLanguage(locale)
    }
}