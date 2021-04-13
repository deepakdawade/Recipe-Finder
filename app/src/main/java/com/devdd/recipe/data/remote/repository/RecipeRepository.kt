package com.devdd.recipe.data.remote.repository

import com.devdd.recipe.data.db.dao.CategoryDao
import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.prefs.manager.GuestManager
import com.devdd.recipe.data.prefs.manager.RecipeManager.Companion.BOTH
import com.devdd.recipe.data.remote.datasource.RecipeDataSource
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface RecipeRepository {

    suspend fun getGuestToken()

    suspend fun getRecipes()

    fun observeRecipes(): Flow<List<Recipe>>

    fun observeRecipesByPref(recipePref: String): Flow<List<Recipe>>

    suspend fun getCategories()

    fun observeCategories(): Flow<List<Category>>

    fun searchRecipes(query: String): Flow<List<Recipe>>

    suspend fun getRecipesByCategories(request: RecipesByCategoryRequest): List<Recipe>

}

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource,
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val guestManager: GuestManager
) : RecipeRepository {

    override suspend fun getGuestToken() {
        if (!guestManager.hasGuestToken()){
            val guestResponse = dataSource.fetchGuestToken()
            val guestToken = guestResponse.guestToken ?: ""
            guestManager.updateGuestToken(guestToken)
        }
    }

    override suspend fun getRecipes() {
        val recipes = dataSource.fetchRecipes()
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

    override fun observeRecipesByPref(recipePref: String): Flow<List<Recipe>> {
        return if (recipePref == BOTH)
            recipeDao.allRecipes()
        else recipeDao.recipesByPref(recipePref)
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