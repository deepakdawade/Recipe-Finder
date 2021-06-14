package com.devdd.recipe.base_android.initializer

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}