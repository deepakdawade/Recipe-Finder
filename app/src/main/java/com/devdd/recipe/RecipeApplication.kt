package com.devdd.recipe

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.devdd.recipe.base.utils.AppBuildConfig
import com.devdd.recipe.base_android.utils.localemanager.LocaleManagerUtils
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