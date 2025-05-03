package com.saeedev.movieadinterviewtest.data.network

import com.saeedev.movieadinterviewtest.data.network.dto.AdvertisesDto
import com.saeedev.movieadinterviewtest.data.network.dto.VideosDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoAdApi {

    @GET("videos/index")
    suspend fun getMovies(
        @Query("offset") offset: Int = 0
    ): VideosDto

    @GET("advertises/index")
    suspend fun getAdvertises(): AdvertisesDto
}