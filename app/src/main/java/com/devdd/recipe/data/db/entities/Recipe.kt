package com.devdd.recipe.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "author_name")
    val authorName: String = "",

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "title_hi")
    val titleHi: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "description_hi")
    val descriptionHi: String = "",

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @ColumnInfo(name = "category_name")
    val categoryName: String = "",

    @ColumnInfo(name = "preparing_time")
    val preparingTime: String = "",

    @ColumnInfo(name = "cooking_time")
    val cookingTime: String = "",

    @ColumnInfo(name = "total_time")
    val totalTime: String = "",

    @ColumnInfo(name = "ingrediants")
    var ingredients: List<String> = emptyList(),

    @ColumnInfo(name = "ingrediants_hi")
    var ingredientsHi: List<String> = emptyList(),

    @ColumnInfo(name = "saved_time")
    var savedTime: Long = 0,

    @ColumnInfo(name = "saved")
    var saved: Boolean = false,

    )