package com.example.barcodescanner4

import android.util.Log
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray

class Converter {

    @TypeConverter
    fun parseListToString(list: List<OpinionOnThrowingAway>): String{
        return list.toString()
    }

    @TypeConverter
    fun parseFromStringToList(string: String): List<OpinionOnThrowingAway> {
        val resultList = mutableListOf<OpinionOnThrowingAway>()
        // Remove square brackets from the string
        val content = string.removeSurrounding("[", "]")
        // Split the string into individual elements
        val elements = content.split("OpinionOnThrowingAway")
        for (element in elements) {
            // Extract values for each property
            val single = element.removeSuffix(", ").removeSurrounding(prefix = "(", suffix = ")")

            val properties = single.split(",")



            if(properties.size == 3){
                val kindOfBasket = properties[0].removePrefix("kind_of_basket=")
                val description = properties[1].removePrefix(" descripton=")
                val date = properties[2].removePrefix(" date=").toLongOrNull() ?: System.currentTimeMillis()

                resultList.add(OpinionOnThrowingAway(kindOfBasket,description,date))
            }
        }
        return resultList
    }
}