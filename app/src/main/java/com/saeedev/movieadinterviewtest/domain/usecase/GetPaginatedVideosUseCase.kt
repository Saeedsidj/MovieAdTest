package com.saeedev.movieadinterviewtest.domain.usecase

import androidx.paging.PagingData
import com.saeedev.movieadinterviewtest.common.Result
import com.saeedev.movieadinterviewtest.domain.model.VideosList
import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetPaginatedVideosUseCase @Inject constructor(
    private val repository: VideoAdRepository
) {
    operator fun invoke(): Flow<PagingData<VideosList>> {
        return repository.getPagedVideos()
    }
}
