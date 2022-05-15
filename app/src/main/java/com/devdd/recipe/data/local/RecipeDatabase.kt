package com.devdd.recipe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devdd.recipe.data.local.converter.RoomConverters
import com.devdd.recipe.data.local.dao.RecipeDao
import com.devdd.recipe.data.local.model.Recipe

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}

