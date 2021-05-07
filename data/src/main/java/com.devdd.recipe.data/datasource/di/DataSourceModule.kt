package com.devdd.recipe.data.datasource.di

import com.devdd.recipe.data.datasource.RecipeDataSource
import com.devdd.recipe.data.datasource.RecipeDataSourceImpl
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