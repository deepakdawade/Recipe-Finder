package com.devdd.recipe.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerResponse<T>(

    @SerializedName("status")
    @Expose
    var status: Int = 0,

    @SerializedName("message")
    @Expose
    var message: String = "",

    @SerializedName("errCode")
    @Expose
    var errCode: Int = -1,

    @SerializedName("data")
    @Expose
    var data: T
)