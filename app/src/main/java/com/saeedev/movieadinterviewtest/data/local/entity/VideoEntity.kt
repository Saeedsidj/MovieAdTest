package com.saeedev.movieadinterviewtest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saeedev.movieadinterviewtest.domain.model.VideosList

@Entity(tableName = "video")
data class VideoEntity(
    @PrimaryKey
    val id : Int,
    val title : String,
    val url : String,
    val duration : Double,
    val usersLiked : List<Int>
){
    fun toDomain() = VideosList(
        id = id,
        title = title,
        url = url,
        duration = duration,
        usersLiked = usersLiked
    )
}