package com.devdd.recipe.data.remote.repository

import com.devdd.recipe.data.db.dao.CategoryDao
import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.db.toRecipeViewState
import com.devdd.recipe.data.prefs.manager.GuestManager
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.data.prefs.manager.RecipeManager.Companion.BOTH
import com.devdd.recipe.data.remote.datasource.RecipeDataSource
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface RecipeRepository {

    suspend fun getGuestToken()

    suspend fun getRecipes()

    fun observeRecipes(): Flow<List<Recipe>>

    suspend fun observeRecipesByPref(): Flow<List<RecipeViewState>>

    suspend fun getCategories()

    fun observeCategories(): Flow<List<Category>>

    fun searchRecipes(query: String): Flow<List<Recipe>>

    suspend fun getRecipesByCategories(request: RecipesByCategoryRequest): List<Recipe>

}

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource,
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val guestManager: GuestManager,
    private val localeManager: LocaleManager,
    private val recipeManager: RecipeManager
) : RecipeRepository {

    override suspend fun getGuestToken() {
        if (!guestManager.hasGuestToken()) {
            val guestResponse = dataSource.fetchGuestToken()
            val guestToken = guestResponse.guestToken ?: ""
            guestManager.updateGuestToken(guestToken)
        }
    }

    override suspend fun getRecipes() {
        if (!guestManager.hasGuestToken()) return
        val recipes = dataSource.fetchRecipes(guestManager.guestToken())
        val localRecipes = recipeDao.allRecipes().first()
        val insertIntoDB = recipes.isNotEmpty() && recipes != localRecipes
        if (insertIntoDB) {
            if (localRecipes.isNotEmpty())
                recipeDao.dropRecipes()
            recipeDao.insertRecipe(*recipes.toTypedArray())
        }
    }

    override fun observeRecipes(): Flow<List<Recipe>> {
        return recipeDao.allRecipes()
    }

    override suspend fun observeRecipesByPref(): Flow<List<RecipeViewState>> {
        return localeManager.selectedLanguage.combineTransform(recipeManager.recipePreference) { lang: String, pref: String ->
            emit(Pair(lang == LocaleManager.LOCALE_ENGLISH, pref))
        }.flatMapLatest { pair ->
            val flow = if (pair.second == BOTH)
                recipeDao.allRecipes()
            else recipeDao.recipesByPref(pair.second)
            val viewState = flow.map { recipes ->
                recipes.map { recipe ->
                    recipe.toRecipeViewState(pair.first)
                }
            }
            viewState
        }
    }

    override suspend fun getCategories() {
        val categories = dataSource.fetchCategories()
        val localCategories = categoryDao.allCategories().first()
        val insertIntoDB = categories.isNotEmpty() && categories != localCategories
        if (insertIntoDB) {
            if (localCategories.isNotEmpty())
                categoryDao.dropCategories()
            categoryDao.insertCategory(*categories.toTypedArray())
        }
    }

    override fun observeCategories(): Flow<List<Category>> {
        return categoryDao.allCategories()
    }

    override fun searchRecipes(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes("%$query%")
    }

    override suspend fun getRecipesByCategories(request: RecipesByCategoryRequest): List<Recipe> {
        return dataSource.fetchRecipesByCategory(request)
    }
}