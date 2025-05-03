package com.saeedev.movieadinterviewtest.data.network.dto

import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity

data class VideosDto(
    val done: Boolean,
    val result: VideoResult
)

data class VideoResult(
    val end: Boolean,
    val remaining: Int,
    val videos: List<Video>
)

data class Video(
    val duration: Double,
    val id: Int,
    val title: String,
    val url: String,
){
    fun toEntity() = VideoEntity(
        id = id,
        url = url,
        title = title,
        duration = duration,
        usersLiked = emptyList()
    )
}