package com.devdd.recipe.firebase.channels

import android.app.Application
import android.os.Build
import com.devdd.recipe.base_android.initializer.AppInitializer
import com.devdd.recipe.base_android.utils.extensions.isAtLeastVersion
import com.devdd.recipe.firebase.channels.RecipeNotificationChannels
import javax.inject.Inject


class NotificationChannelsInitializer @Inject constructor(
    private val recipeNotificationChannels: RecipeNotificationChannels
) : AppInitializer {

    override fun init(application: Application) {
        if (isAtLeastVersion(Build.VERSION_CODES.O)) {
            recipeNotificationChannels.createNotificationChannels()
        }
    }

}