package com.devdd.recipe

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.devdd.recipe.utils.AppBuildConfig
import com.devdd.recipe.utils.localemanager.LocaleManagerUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RecipeApplication : Application() {
    @Inject
    lateinit var buildConfig: AppBuildConfig
    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    override fun onCreate() {
        super.onCreate()
        if (buildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(base: Context) {
        scope.launch {
            super.attachBaseContext(LocaleManagerUtils.setLocale(base))
            Timber.d("attachBaseContext")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        scope.launch {
            LocaleManagerUtils.setLocale(this@RecipeApplication)
            Timber.d(
                "onConfigurationChanged: %s",
                LocaleManagerUtils.getLanguage(this@RecipeApplication)
            )
        }
    }
}