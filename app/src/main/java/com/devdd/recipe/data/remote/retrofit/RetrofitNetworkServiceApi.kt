package com.devdd.recipe.data.remote.retrofit

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.data.remote.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.remote.models.request.SavedRecipesRequest
import com.devdd.recipe.data.remote.models.response.GuestResponse
import com.devdd.recipe.data.remote.models.response.RecipeListResponse
import retrofit2.http.Body
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
    suspend fun recipes(@Query("guest_token") guestToken: String): ServerResponse<RecipeListResponse>

    /**
     * register device @param device_id
     * */
    @POST(EndPoints.DEVICES)
    suspend fun devices(@Query("device_id") deviceId: String): ServerResponse<Any>

    /**
     * mark recipe favorite @param guest_token and recipe_id
     * */
    @POST(EndPoints.FAVORITE_RECIPES)
    suspend fun markRecipeFavorite(@Body request: MarkRecipeFavoriteRequest): ServerResponse<Any>

    /**
     * fetch saved recipes @param guest_token
     * */
    @POST(EndPoints.SAVED_RECIPES)
    suspend fun savedRecipes(@Body request: SavedRecipesRequest): ServerResponse<RecipeListResponse>

}