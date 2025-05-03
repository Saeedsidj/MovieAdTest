package com.saeedev.movieadinterviewtest.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConverters {

    @TypeConverter
    fun encodeLikedVideosIdFromJason(likedVideos : List<Int>) : String {
        return Json.encodeToString(likedVideos)
    }

    @TypeConverter
    fun decodeLikedVideosIdFromJason(json: String): List<Int> {
        return if (json.isBlank()) emptyList()
        else Json.decodeFromString(json)
    }
}