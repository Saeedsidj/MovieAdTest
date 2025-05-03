package com.saeedev.movieadinterviewtest.data.local.dataSource

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.saeedev.movieadinterviewtest.data.local.AppDataBase
import com.saeedev.movieadinterviewtest.data.local.entity.AdvertiseEntity
import com.saeedev.movieadinterviewtest.data.local.entity.UserEntity
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataBase: AppDataBase
) : LocalDataSource {

    override fun observeAllUsers(): Flow<List<UserEntity>> {
        return dataBase.userDao().getUsers()
    }

    override suspend fun insertAdvertises(advertises: List<AdvertiseEntity>) {
        dataBase.advertiseDao().insertAll(advertises)
    }

    override suspend fun observeAdvertises(): List<AdvertiseEntity> {
        return dataBase.advertiseDao().getAllAdvertises()
    }

    override suspend fun toggleLikeVideo(userId: Int, videoId: Int) {
        val video = dataBase.videoDao().getVideoById(videoId) ?: return
        val newLikedBy = if (video.usersLiked.contains(userId)) {
            video.usersLiked - userId
        } else {
            video.usersLiked + userId
        }
        val updatedVideo = video.copy(usersLiked = newLikedBy)
        dataBase.videoDao().updateVideo(updatedVideo)
    }

    override fun getAllPaginatedVideos(): PagingSource<Int, VideoEntity> {
        return dataBase.videoDao().getAllPaginatedVideos()
    }

    override fun getAllVideos(): Flow<List<VideoEntity>> {
        return dataBase.videoDao().getAllVideos()
    }
    override fun insertAllVideos(videos: List<VideoEntity>) {
        dataBase.videoDao().insertAll(videos)
    }

    override suspend fun clearAllVideos() {
        dataBase.videoDao().clearAll()
    }

    override suspend fun <R> inTransaction(block: suspend () -> R): R {
        return dataBase.withTransaction {
            block()
        }
    }

    override suspend fun setNewUserLogin(userId: Int) {
        val result = dataBase.userDao().logoutAllUsers()
        dataBase.userDao().loginUserById(userId)
    }
}