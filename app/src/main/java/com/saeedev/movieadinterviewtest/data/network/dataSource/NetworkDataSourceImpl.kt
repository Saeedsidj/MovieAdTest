package com.saeedev.movieadinterviewtest.data.network.dataSource

import com.saeedev.movieadinterviewtest.data.network.VideoAdApi
import com.saeedev.movieadinterviewtest.data.network.dto.AdvertisesDto
import com.saeedev.movieadinterviewtest.data.network.dto.VideosDto
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val api: VideoAdApi
) : NetworkDataSource {
    override suspend fun getMovies(offset: Int): VideosDto {
        return api.getMovies(offset)
    }

    override suspend fun getAdvertises(): AdvertisesDto {
        return api.getAdvertises()
    }
}