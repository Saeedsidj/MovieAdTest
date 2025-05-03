package com.saeedev.movieadinterviewtest.data.local.dataSource

import androidx.paging.PagingSource
import com.saeedev.movieadinterviewtest.data.local.entity.AdvertiseEntity
import com.saeedev.movieadinterviewtest.data.local.entity.UserEntity
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun observeAllUsers(): Flow<List<UserEntity>>

    suspend fun insertAdvertises(advertises: List<AdvertiseEntity>)

    suspend fun observeAdvertises(): List<AdvertiseEntity>

    suspend fun toggleLikeVideo(userId: Int, videoId: Int)

    fun getAllPaginatedVideos(): PagingSource<Int, VideoEntity>

    fun getAllVideos(): Flow<List<VideoEntity>>

    fun insertAllVideos(videos: List<VideoEntity>)

    suspend fun clearAllVideos()

    suspend fun setNewUserLogin(userId: Int)

    suspend fun <R> inTransaction(block: suspend () -> R): R


}