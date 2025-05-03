package com.saeedev.movieadinterviewtest.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.saeedev.movieadinterviewtest.data.local.dataSource.LocalDataSource
import com.saeedev.movieadinterviewtest.data.local.mediator.VideoRemoteMediator
import com.saeedev.movieadinterviewtest.data.network.dataSource.NetworkDataSource
import com.saeedev.movieadinterviewtest.domain.model.AdvertisesList
import com.saeedev.movieadinterviewtest.domain.model.User
import com.saeedev.movieadinterviewtest.domain.model.VideosList
import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoAdRepositoryImpl @Inject constructor(
    private val api: NetworkDataSource,
    private val db: LocalDataSource
) : VideoAdRepository {

        @OptIn(ExperimentalPagingApi::class)
        override fun getPagedVideos(): Flow<PagingData<VideosList>> {
            return Pager(
                config = PagingConfig(pageSize = 5),
                remoteMediator = VideoRemoteMediator(api, db),
                pagingSourceFactory = { db.getAllPaginatedVideos() }
            ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }

        }

    override suspend fun getAdvertises(): List<AdvertisesList> {
        return db.observeAdvertises().map { it.toDomain() }
    }

    override fun getUsers(): Flow<List<User>> {
        return db.observeAllUsers().map { it.map { it.toDomain() } }
    }

    override suspend fun likeVideo(userid: Int, videoId: Int) {
        db.toggleLikeVideo(userid, videoId)
    }

    override suspend fun loginNewUser(userid: Int) {
        db.setNewUserLogin(userid)
    }

    override fun getAllVideos(): Flow<List<VideosList>> {
        return db.getAllVideos().map { it.map { it.toDomain() }}
    }
}
