package com.saeedev.movieadinterviewtest.data.local.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.saeedev.movieadinterviewtest.data.local.dataSource.LocalDataSource
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity
import com.saeedev.movieadinterviewtest.data.network.dataSource.NetworkDataSource

@OptIn(ExperimentalPagingApi::class)
class VideoRemoteMediator(
    private val api: NetworkDataSource,
    private val db: LocalDataSource
) : RemoteMediator<Int, VideoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VideoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                state.pages.sumOf { it.data.size }
            }
        }

        return try {
            val advertises = api.getAdvertises().result.advertises.map{it.toEntity()}
            db.insertAdvertises(advertises)
            val response = api.getMovies(offset = page)
            val videos = response.result.videos.map { it.toEntity() }

            db.inTransaction {
                db.insertAllVideos(videos)
            }



            MediatorResult.Success(endOfPaginationReached = videos.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
