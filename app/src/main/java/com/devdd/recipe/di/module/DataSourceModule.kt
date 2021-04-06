package com.devdd.recipe.di.module

import com.devdd.recipe.data.remote.datasource.RecipeDataSource
import com.devdd.recipe.data.remote.datasource.RecipeDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRecipeDataSource(dataSource: RecipeDataSourceImpl): RecipeDataSource = dataSource
}