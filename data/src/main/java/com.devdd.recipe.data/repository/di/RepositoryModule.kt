package com.devdd.recipe.data.repository.di

import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.data.repository.FirebaseRepositoryImpl
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

    @Provides
    @Singleton
    fun provideFirebaseRepository(repository: FirebaseRepositoryImpl): FirebaseRepository = repository
}