package com.devdd.recipe.di

import android.content.Context
import androidx.room.Room
import com.devdd.recipe.data.local.RecipeDatabase
import com.devdd.recipe.data.local.dao.RecipeDao
import com.devdd.recipe.utils.DB_NAME
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
    fun provideDao(database: RecipeDatabase): RecipeDao = database.recipeDao()

    @Singleton
    @Provides
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(context, RecipeDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }
}