package com.devdd.recipe.base.utils

import com.devdd.recipe.base.utils.extensions.BuildType


/*
* Share app build configuration across all module
* */
data class AppBuildConfig(

    @JvmField
    val DEBUG: Boolean,

    @JvmField
    val APPLICATION_ID: String,

    @JvmField
    val BUILD_TYPE: BuildType,

    @JvmField
    val VERSION_CODE: Int,

    @JvmField
    val VERSION_NAME: String,

    @JvmField
    val BASE_URL: String,
)
