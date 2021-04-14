package com.devdd.recipe.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Recipe


@Database(entities = [Recipe::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}