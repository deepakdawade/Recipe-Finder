package com.devdd.recipe.data.room.di

import android.content.Context
import androidx.room.Room
import com.devdd.recipe.base.constants.DB_NAME
import com.devdd.recipe.data.room.RecipeDatabase
import com.devdd.recipe.data.room.dao.RecipeDao
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

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext appContext: Context): RecipeDatabase {
        return Room.databaseBuilder(
            appContext,
            RecipeDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

}