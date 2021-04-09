package com.devdd.recipe.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    )