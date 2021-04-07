package com.devdd.recipe.data.remote.retrofit

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.data.remote.models.response.CategoryListResponse
import com.devdd.recipe.data.remote.models.response.RecipeListResponse
import retrofit2.http.GET

interface RetrofitNetworkServiceApi {
    @GET(EndPoints.RECIPES)
    suspend fun recipes(): ServerResponse<RecipeListResponse>

    @GET(EndPoints.CATEGORIES)
    suspend fun categories(): ServerResponse<CategoryListResponse>
}