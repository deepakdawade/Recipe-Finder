package com.devdd.recipe.data.remote.repository

import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.prefs.manager.RecipeManager.Companion.BOTH
import com.devdd.recipe.data.remote.datasource.RecipeDataSource
import com.devdd.recipe.data.remote.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.remote.models.request.SavedRecipesRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface RecipeRepository {

    suspend fun getGuestToken(): String

    suspend fun getRecipes(guestToken: String)

    suspend fun setDeviceId(deviceId: String)

    suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest)

    suspend fun getSavedRecipes(request: SavedRecipesRequest)

    fun observeRecipes(): Flow<List<Recipe>>

    fun observeRecipesByPref(pref: String): Flow<List<Recipe>>

    fun searchRecipes(query: String): Flow<List<Recipe>>

    fun getRecipeById(id:Int):Flow<Recipe>

}

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun getGuestToken(): String {
        val guestResponse = dataSource.fetchGuestToken()
        return guestResponse.guestToken ?: ""
    }

    override suspend fun getRecipes(guestToken: String) {
        val recipes = dataSource.fetchRecipes(guestToken)
        val localRecipes = recipeDao.allRecipes().first()
        val insertIntoDB = recipes.isNotEmpty() && recipes != localRecipes
        if (insertIntoDB) {
            if (localRecipes.isNotEmpty())
                recipeDao.dropRecipes()
            recipeDao.insertRecipe(*recipes.toTypedArray())
        }
    }

    override suspend fun setDeviceId(deviceId: String) {
        dataSource.setDeviceId(deviceId)
    }

    override suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest) {
        dataSource.markRecipeFavorite(request)
        val recipe = recipeDao.recipeById(request.recipeId).first()
        recipe.apply { saved = request.saved }
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun getSavedRecipes(request: SavedRecipesRequest) {
        val recipes = dataSource.fetchSavedRecipes(request)
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

    override fun observeRecipesByPref(pref: String): Flow<List<Recipe>> {
        return if (pref == BOTH)
            recipeDao.allRecipes()
        else recipeDao.recipesByPref(pref)
    }

    override fun searchRecipes(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes("%$query%")
    }

    override fun getRecipeById(id: Int): Flow<Recipe> {
        return recipeDao.recipeById(id)
    }
}