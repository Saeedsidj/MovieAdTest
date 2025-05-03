package com.saeedev.movieadinterviewtest.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM video")
    fun getAllPaginatedVideos(): PagingSource<Int, VideoEntity>

    @Query("SELECT * FROM video")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Upsert
    fun insertAll(videos: List<VideoEntity>)

    @Query("DELETE FROM video")
    suspend fun clearAll()

    @Query("SELECT * FROM video WHERE id = :videoId")
    suspend fun getVideoById(videoId: Int): VideoEntity?


    @Update
    suspend fun updateVideo(video: VideoEntity)
}