package com.devdd.recipe

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.devdd.recipe.base.utils.AppBuildConfig
import com.devdd.recipe.base.utils.RecipeAppConfig
import com.devdd.recipe.base_android.initializer.AppInitializers
import com.devdd.recipe.base_android.utils.localemanager.LocaleManagerUtils
import com.devdd.recipe.ui.MainActivity
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RecipeApplication : Application() {
    @Inject
    lateinit var buildConfig: AppBuildConfig

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        if (buildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        initializers.init(this)

        RecipeAppConfig.appBuildConfig = buildConfig
        RecipeAppConfig.mainActivityClassName = MainActivity::class.java
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManagerUtils.setLocale(base))
        Timber.d("attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManagerUtils.setLocale(this)
        Timber.d("onConfigurationChanged: %s", LocaleManagerUtils.getLanguage(this))
    }
}