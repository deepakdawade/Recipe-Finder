package com.devdd.recipe.data.local.converter

import androidx.room.TypeConverter
import com.devdd.recipe.api.toDataClass
import com.devdd.recipe.api.toJsonString
object RoomConverters {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String): List<String> = value.toDataClass()

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<String>): String = list.toJsonString()
}