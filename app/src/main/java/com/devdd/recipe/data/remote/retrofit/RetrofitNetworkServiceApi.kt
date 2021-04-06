package com.devdd.recipe.data.remote.retrofit

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.data.remote.models.response.RecipeListResponse
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by @author Deepak Dawade on 4/6/2021 at 12:53 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
interface RetrofitNetworkServiceApi {
    @GET(EndPoints.GET_ALL_RECIPES)
    suspend fun getAllRecipes(): ServerResponse<RecipeListResponse>
}