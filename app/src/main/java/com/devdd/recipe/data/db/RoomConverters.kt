package com.devdd.recipe.data.db

import androidx.room.TypeConverter
import com.devdd.recipe.utils.extensions.toDataClass
import com.devdd.recipe.utils.extensions.toJsonString


object RoomConverters {
    @TypeConverter
    fun fromString(value: String): List<String> = value.toDataClass()

    @TypeConverter
    fun fromList(list: List<String>): String = list.toJsonString()
}