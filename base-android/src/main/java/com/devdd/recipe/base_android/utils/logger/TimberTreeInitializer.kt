package com.devdd.recipe.base_android.utils.logger

import android.app.Application
import com.devdd.recipe.base.utils.Logger
import com.devdd.recipe.base_android.initializer.AppInitializer
import javax.inject.Inject

class TimberTreeInitializer @Inject constructor(private val logger: Logger) : AppInitializer {

    override fun init(application: Application): Unit = logger.init()

}