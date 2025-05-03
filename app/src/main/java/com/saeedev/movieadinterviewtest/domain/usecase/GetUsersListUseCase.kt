package com.saeedev.movieadinterviewtest.domain.usecase

import com.saeedev.movieadinterviewtest.common.Result
import com.saeedev.movieadinterviewtest.domain.model.User
import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetUsersListUseCase @Inject constructor(
    private val repository: VideoAdRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> {
        return repository.getUsers()
            .map { userList ->
                Result.Success(userList)
            }
            .onStart { Result.Loading }
            .catch { e -> Result.Error(e) }
    }
}