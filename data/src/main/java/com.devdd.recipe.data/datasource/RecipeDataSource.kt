package com.devdd.recipe.data.datasource

import com.devdd.recipe.data.mappers.RecipeResponseToRecipeEntity
import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.models.request.SavedRecipesRequest
import com.devdd.recipe.data.models.response.GuestResponse
import com.devdd.recipe.data.retrofit.service.RetrofitNetworkServiceApi
import com.devdd.recipe.data.utils.dataOrThrowException
import javax.inject.Inject


interface RecipeDataSource {

    suspend fun fetchGuestToken(): GuestResponse

    suspend fun fetchRecipes(guestToken: String): List<Recipe>

    suspend fun setDeviceId(deviceId: String): Any

    suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest): Any

    suspend fun fetchSavedRecipes(request: SavedRecipesRequest): List<Recipe>
}

class RecipeDataSourceImpl @Inject constructor(
    private val networkServiceApi: RetrofitNetworkServiceApi,
    private val recipeResponseToRecipeEntity: RecipeResponseToRecipeEntity
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

    override suspend fun setDeviceId(deviceId: String): Any {
        val response = networkServiceApi.devices(deviceId)
        return response.dataOrThrowException()
    }

    override suspend fun markRecipeFavorite(request: MarkRecipeFavoriteRequest): Any {
        val response = networkServiceApi.markRecipeFavorite(request)
        return response.dataOrThrowException()
    }

    override suspend fun fetchSavedRecipes(request: SavedRecipesRequest): List<Recipe> {
        val response = networkServiceApi.savedRecipes(request)
        val fetchedRecipes = response.dataOrThrowException()
        return fetchedRecipes.recipes?.map {
            recipeResponseToRecipeEntity.map(it)
        } ?: emptyList()
    }
}