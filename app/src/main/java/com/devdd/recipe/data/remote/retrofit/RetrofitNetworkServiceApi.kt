package com.devdd.recipe.data.remote.retrofit

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.data.remote.models.response.GuestResponse
import com.devdd.recipe.data.remote.models.response.RecipeListResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitNetworkServiceApi {
    /**
     * generate guest token
     * */
    @POST(EndPoints.GUESTS)
    suspend fun guests(): ServerResponse<GuestResponse>

    /**
     * fetch recipes by @param guest_token
     * */
    @GET(EndPoints.RECIPES)
    suspend fun recipes(@Query("guest_token") guest_token: String): ServerResponse<RecipeListResponse>

}