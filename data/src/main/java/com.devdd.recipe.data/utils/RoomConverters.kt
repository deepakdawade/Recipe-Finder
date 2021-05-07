package com.devdd.recipe.data.utils

import androidx.room.TypeConverter


object RoomConverters {
    @TypeConverter
    fun fromString(value: String): List<String> = value.toDataClass()

    @TypeConverter
    fun fromList(list: List<String>): String = list.toJsonString()
}