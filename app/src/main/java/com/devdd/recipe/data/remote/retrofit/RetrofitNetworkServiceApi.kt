package com.devdd.recipe.data.remote.retrofit

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.data.remote.models.request.FetchRecipeRequest
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.data.remote.models.response.CategoryListResponse
import com.devdd.recipe.data.remote.models.response.GuestResponse
import com.devdd.recipe.data.remote.models.response.RecipeListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitNetworkServiceApi {
    /**
     * generate guest token
     * */
    @POST(EndPoints.GUESTS)
    suspend fun guests(): ServerResponse<GuestResponse>

    @GET(EndPoints.RECIPES)
    suspend fun recipes(@Body fetchRecipeRequest: FetchRecipeRequest): ServerResponse<RecipeListResponse>

    @GET(EndPoints.CATEGORIES)
    suspend fun categories(): ServerResponse<CategoryListResponse>

    @POST(EndPoints.RECIPES_BY_CATEGORY)
    suspend fun recipesByCategory(@Body request: RecipesByCategoryRequest): ServerResponse<RecipeListResponse>
}