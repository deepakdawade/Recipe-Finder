package com.devdd.recipe.data.remote.datasource

import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.data.remote.models.response.GuestResponse
import com.devdd.recipe.data.remote.retrofit.RetrofitNetworkServiceApi
import com.devdd.recipe.domain.mappers.CategoryResponseToCategoryEntity
import com.devdd.recipe.domain.mappers.RecipeResponseToRecipeEntity
import com.devdd.recipe.utils.extensions.dataOrThrowException
import javax.inject.Inject


interface RecipeDataSource {

    suspend fun fetchGuestToken(): GuestResponse

    suspend fun fetchRecipes(guestToken: String): List<Recipe>

    suspend fun fetchCategories(): List<Category>

    suspend fun fetchRecipesByCategory(request: RecipesByCategoryRequest): List<Recipe>
}

class RecipeDataSourceImpl @Inject constructor(
    private val networkServiceApi: RetrofitNetworkServiceApi,
    private val recipeResponseToRecipeEntity: RecipeResponseToRecipeEntity,
    private val categoryResponseToCategoryEntity: CategoryResponseToCategoryEntity
) : RecipeDataSource {

    override suspend fun fetchGuestToken(): GuestResponse {
        val response = networkServiceApi.guests()
        return response.dataOrThrowException()
    }

    override suspend fun fetchRecipes(guestToken: String): List<Recipe> {
        val response = networkServiceApi.recipes(guestToken)
        val fetchedRecipes = response.dataOrThrowException()
        return fetchedRecipes.recipes?.map {
            recipeResponseToRecipeEntity.map(it)
        } ?: emptyList()
    }

    override suspend fun fetchCategories(): List<Category> {
        val response = networkServiceApi.categories()
        val categories = response.dataOrThrowException().categories
        return categories?.map { categoryResponseToCategoryEntity.map(it) } ?: emptyList()

    }

    override suspend fun fetchRecipesByCategory(request: RecipesByCategoryRequest): List<Recipe> {
        val response = networkServiceApi.recipesByCategory(request)
        val fetchedRecipes = response.dataOrThrowException()
        return fetchedRecipes.recipes?.map {
            recipeResponseToRecipeEntity.map(it)
        } ?: emptyList()
    }
}