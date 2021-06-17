package com.devdd.recipe.firebase

import android.app.Application
import com.devdd.recipe.base.utils.extensions.debugElseRelease
import com.devdd.recipe.base_android.initializer.AppInitializer
import com.devdd.recipe.base_android.utils.extensions.buildType
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject

class FirebaseRemoteConfigInitializer @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : AppInitializer {

    override fun init(application: Application) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = buildType.debugElseRelease({ 0 }, { 3600 })
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

}