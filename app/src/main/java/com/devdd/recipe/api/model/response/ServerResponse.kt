package com.devdd.recipe.api.model.response

import com.google.gson.annotations.SerializedName

data class ServerResponse<T>(

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("errCode")
    val errCode: Int = -1,

    @SerializedName("data")
    val data: T
)
class ServerException(val errCode: Int, val msg: String) : Throwable(msg)

fun <T> ServerResponse<T>.dataOrThrowException(executeOnSuccess: T.() -> Unit = {}): T {
    return if (status == 1) data.also { executeOnSuccess(it) }
    else throw ServerException(errCode, message)
}