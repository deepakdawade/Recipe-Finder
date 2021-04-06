package com.devdd.recipe.data.remote.datasource

import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.remote.retrofit.RetrofitNetworkServiceApi
import com.devdd.recipe.domain.mappers.RecipeResponseToRecipeEntity
import com.devdd.recipe.utils.extensions.dataOrThrowException
import javax.inject.Inject


interface RecipeDataSource {
    suspend fun getAllRecipes(): List<Recipe>
}

class RecipeDataSourceImpl @Inject constructor(
    private val networkServiceApi: RetrofitNetworkServiceApi,
    private val recipeResponseToRecipeEntity: RecipeResponseToRecipeEntity
) : RecipeDataSource {
    override suspend fun getAllRecipes(): List<Recipe> {
        val response = networkServiceApi.getAllRecipes()
        val fetchedRecipes = response.dataOrThrowException()
        return fetchedRecipes.recipes?.map {
            recipeResponseToRecipeEntity.map(it)
        } ?: emptyList()
    }
}