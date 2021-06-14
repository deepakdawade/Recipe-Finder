package com.devdd.recipe.di.module

import com.devdd.recipe.base_android.initializer.AppInitializer
import com.devdd.recipe.firebase.channels.NotificationChannelsInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
abstract class InitializerModule {

    @Binds
    @IntoSet
    abstract fun provideNotificationChannelsInitializer(bind: NotificationChannelsInitializer): AppInitializer
}