package com.saeedev.movieadinterviewtest.domain.usecase

import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import javax.inject.Inject

class SetNewUserLoginUseCase @Inject constructor(
    private val repository: VideoAdRepository
) {
    suspend operator fun invoke(userId: Int) {
        repository.loginNewUser(userId)
    }

}