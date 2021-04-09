package com.devdd.recipe.data.db.dao

import androidx.room.*
import com.devdd.recipe.data.db.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun allRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun recipeById(id:Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(vararg recipe: Recipe)

    @Query("Select * FROM recipes where title like  :title")
    fun searchRecipes(title: String): Flow<List<Recipe>>

    @Query("DELETE FROM recipes")
    fun dropRecipes()

}