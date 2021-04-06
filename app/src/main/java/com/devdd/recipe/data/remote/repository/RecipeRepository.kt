package com.devdd.recipe.data.remote.repository

import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.remote.datasource.RecipeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface RecipeRepository {
    suspend fun fetchAllRecipes()
    fun observeAllRecipes(): Flow<List<Recipe>>
}

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun fetchAllRecipes() {
        val recipes = dataSource.getAllRecipes()
        val localRecipes = recipeDao.getAllRecipes().first()
        localRecipes.map { it.apply { key = 0 } }
        if (localRecipes != recipes && recipes.isNotEmpty())
            recipeDao.insertRecipe(*recipes.toTypedArray())
    }

    override fun observeAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }
}