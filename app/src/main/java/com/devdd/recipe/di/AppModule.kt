package com.devdd.recipe.di

import android.content.Context
import android.content.SharedPreferences
import com.devdd.recipe.data.local.pref.DataStorePreference
import com.devdd.recipe.data.local.pref.DataStorePreferences
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.data.remote.repository.RecipeRepositoryImpl
import com.devdd.recipe.data.remote.source.RecipeDataSource
import com.devdd.recipe.data.remote.source.RecipeDataSourceImpl
import com.devdd.recipe.utils.SHARED_PREF_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRecipeDataSource(dataSource: RecipeDataSourceImpl): RecipeDataSource = dataSource

    @Provides
    @Singleton
    fun provideRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository = repository

    @Singleton
    @Provides
    fun providesRecipeDataStore(dataStore: DataStorePreferences): DataStorePreference = dataStore

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
}