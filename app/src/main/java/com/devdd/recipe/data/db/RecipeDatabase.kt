package com.devdd.recipe.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devdd.recipe.data.db.dao.CategoryDao
import com.devdd.recipe.data.db.dao.RecipeDao
import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.db.entities.Recipe


@Database(entities = [Recipe::class, Category::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun categoryDao(): CategoryDao
}