package com.saeedev.movieadinterviewtest.domain.repository

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.saeedev.movieadinterviewtest.data.network.dto.AdvertisesDto
import com.saeedev.movieadinterviewtest.data.network.dto.VideosDto
import com.saeedev.movieadinterviewtest.domain.model.AdvertisesList
import com.saeedev.movieadinterviewtest.domain.model.User
import com.saeedev.movieadinterviewtest.domain.model.VideosList
import kotlinx.coroutines.flow.Flow

interface VideoAdRepository {
    fun getPagedVideos(): Flow<PagingData<VideosList>>
    fun getAllVideos() : Flow<List<VideosList>>
    suspend fun getAdvertises(): List<AdvertisesList>
    fun getUsers(): Flow<List<User>>
    suspend fun likeVideo(userid: Int, videoId: Int)
   suspend fun loginNewUser(userid: Int)
}