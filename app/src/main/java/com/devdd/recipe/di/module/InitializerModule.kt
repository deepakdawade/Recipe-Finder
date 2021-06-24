package com.devdd.recipe.di.module

import com.devdd.recipe.base_android.initializer.AppInitializer
import com.devdd.recipe.base_android.utils.logger.TimberTreeInitializer
import com.devdd.recipe.firebase.FirebaseRemoteConfigInitializer
import com.devdd.recipe.firebase.channels.NotificationChannelsInitializer
import com.devdd.recipe.ui.utils.CoilInitializer
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


    @Binds
    @IntoSet
    abstract fun provideTimberTreeInitializer(bind: TimberTreeInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideFirebaseRemoteConfigInitializer(bind: FirebaseRemoteConfigInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilInitializer): AppInitializer

}