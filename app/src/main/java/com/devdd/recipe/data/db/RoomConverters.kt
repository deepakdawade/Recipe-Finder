package com.devdd.recipe.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object RoomConverters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}