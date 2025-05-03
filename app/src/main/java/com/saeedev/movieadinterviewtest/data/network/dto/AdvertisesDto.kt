package com.saeedev.movieadinterviewtest.data.network.dto

import com.saeedev.movieadinterviewtest.data.local.entity.AdvertiseEntity

data class AdvertisesDto(
    val done: Boolean,
    val result: AdvertisesResult
)

data class AdvertisesResult(
    val advertises: List<Advertise>
)

data class Advertise(
    val duration: Double,
    val id: Int,
    val title: String,
    val url: String
){
    fun toEntity() = AdvertiseEntity(
        id = id,
        title = title,
        duration = duration,
        url = url
    )
}