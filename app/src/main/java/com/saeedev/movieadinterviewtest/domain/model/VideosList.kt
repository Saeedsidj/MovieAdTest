package com.saeedev.movieadinterviewtest.domain.model

data class VideosList(
    val id : Int,
    val title : String,
    val url : String,
    val duration: Double,
    val usersLiked : List<Int>
)
