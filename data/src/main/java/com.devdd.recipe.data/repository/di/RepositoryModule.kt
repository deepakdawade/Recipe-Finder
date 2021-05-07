package com.devdd.recipe.data.repository.di

import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.data.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository = repository
}