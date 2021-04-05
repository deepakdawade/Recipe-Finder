package com.devdd.recipe

import android.app.Application
import com.devdd.recipe.utils.AppBuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RecipeApplication : Application() {
    @Inject
    lateinit var buildConfig: AppBuildConfig
    override fun onCreate() {
        super.onCreate()
        if (buildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}