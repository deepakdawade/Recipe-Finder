package com.devdd.recipe.domain.result

class ServerException(val errCode: Int, val msg: String) : Exception(msg)
