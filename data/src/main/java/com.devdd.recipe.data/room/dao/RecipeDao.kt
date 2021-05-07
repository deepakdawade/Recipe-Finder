package com.devdd.recipe.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devdd.recipe.data.models.entity.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun allRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category_name = :category")
    fun recipesByPref(category: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun recipeById(id: Int): Flow<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(vararg recipe: Recipe)

    @Query("Select * FROM recipes where title like  :query OR title_hi like :query")
    fun searchRecipes(query: String): Flow<List<Recipe>>

    @Query("DELETE FROM recipes")
    fun dropRecipes()

}