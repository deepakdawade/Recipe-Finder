package com.devdd.recipe.di.module

import android.content.Context
import androidx.room.Room
import com.devdd.recipe.constants.DB_NAME
import com.devdd.recipe.data.db.RecipeDatabase
import com.devdd.recipe.data.db.dao.CategoryDao
import com.devdd.recipe.data.db.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao = database.recipeDao()

    @Singleton
    @Provides
    fun provideCategoryDao(database: RecipeDatabase): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext appContext: Context): RecipeDatabase {
        return Room.databaseBuilder(
            appContext,
            RecipeDatabase::class.java,
            DB_NAME
        ).build()
    }

}