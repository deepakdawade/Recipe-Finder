package com.devdd.recipe.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devdd.recipe.data.room.dao.RecipeDao
import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.utils.RoomConverters


@Database(entities = [Recipe::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}