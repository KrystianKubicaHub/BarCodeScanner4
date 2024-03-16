package com.example.barcodescanner4

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.json.JSONArray

class Converter {

    @TypeConverter
    fun fromListOfOpinionsToJson(list : List<OpinionOnThrowingAway>) : String{
        val result = list.toString()
        return result
    }

    @TypeConverter
    fun fromStringToListOfOpinions(string : String) : List<OpinionOnThrowingAway>{
        val list = listOf<OpinionOnThrowingAway>()
        return list
    }
}