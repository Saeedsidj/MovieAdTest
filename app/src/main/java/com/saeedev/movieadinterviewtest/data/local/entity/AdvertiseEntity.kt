package com.saeedev.movieadinterviewtest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saeedev.movieadinterviewtest.domain.model.AdvertisesList

@Entity("advertise")
data class AdvertiseEntity(
    @PrimaryKey
    val id: Int,
    val duration: Double,
    val title: String,
    val url: String
){
    fun toDomain() = AdvertisesList(
        id = id,
        duration = duration,
        title = title,
        url = url
    )
}

