package com.saeedev.movieadinterviewtest.domain.usecase

import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import javax.inject.Inject

class LikeVideoUseCase @Inject constructor(
    private val repository: VideoAdRepository
) {
    suspend operator fun invoke(userId: Int, videoId: Int) {
        repository.likeVideo(userId, videoId)
    }

}