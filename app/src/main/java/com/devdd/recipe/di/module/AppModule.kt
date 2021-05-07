package com.devdd.recipe.di.module

import com.devdd.recipe.BuildConfig
import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.BuildConfig as UrlConfig
import com.devdd.recipe.data.preference.DataStorePreference
import com.devdd.recipe.data.preference.DataStorePreferences
import com.devdd.recipe.utils.AppBuildConfig
import com.devdd.recipe.utils.extensions.buildType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRecipeDataStore(dataStore: DataStorePreferences): DataStorePreference = dataStore

    @Singleton
    @Provides
    fun provideAppBuildConfig() = AppBuildConfig(
        DEBUG = BuildConfig.DEBUG,
        APPLICATION_ID = BuildConfig.APPLICATION_ID,
        VERSION_CODE = BuildConfig.VERSION_CODE,
        VERSION_NAME = BuildConfig.VERSION_NAME,
        BASE_URL = UrlConfig.BASE_URL,
        BUILD_TYPE = buildType
    )

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

}