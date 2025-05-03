package com.saeedev.movieadinterviewtest.data.network.dataSource

import com.saeedev.movieadinterviewtest.data.network.dto.AdvertisesDto
import com.saeedev.movieadinterviewtest.data.network.dto.VideosDto

interface NetworkDataSource {

    suspend fun getMovies(offset: Int): VideosDto

    suspend fun getAdvertises(): AdvertisesDto

}