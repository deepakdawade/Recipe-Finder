package com.devdd.recipe.base.result

class ServerException(val errCode: Int, val msg: String) : Throwable(msg)
