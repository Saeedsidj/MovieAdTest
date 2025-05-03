package com.saeedev.movieadinterviewtest.domain.usecase

import com.saeedev.movieadinterviewtest.common.Result
import com.saeedev.movieadinterviewtest.domain.model.AdvertisesList
import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class GetAdvertisesUseCase @Inject constructor(
    private val repository: VideoAdRepository,
) {
    suspend operator fun invoke(): List<AdvertisesList> =
        repository.getAdvertises()
}