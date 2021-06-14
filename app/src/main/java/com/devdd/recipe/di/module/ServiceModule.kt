package com.devdd.recipe.di.module

import com.devdd.recipe.firebase.service.RecipeFirebaseMessagingService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ServiceComponent::class)
@Module
abstract class ServiceModule {

    @Binds
    abstract fun fcmService() : RecipeFirebaseMessagingService
}