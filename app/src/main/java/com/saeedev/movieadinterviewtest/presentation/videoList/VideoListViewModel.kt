package com.saeedev.movieadinterviewtest.presentation.videoList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.saeedev.movieadinterviewtest.common.onSuccess
import com.saeedev.movieadinterviewtest.domain.model.User
import com.saeedev.movieadinterviewtest.domain.model.VideosList
import com.saeedev.movieadinterviewtest.domain.usecase.GetUsersListUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.GetPaginatedVideosUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.LikeVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val getVideosListUseCase: GetPaginatedVideosUseCase,
    private val getUsersListUseCase: GetUsersListUseCase,
    private val likeVideoUseCase: LikeVideoUseCase
) : ViewModel() {

    val videos = getVideosListUseCase()
        .map { it.map { video -> video.toUi() } }
        .cachedIn(viewModelScope)

    private val _user: MutableStateFlow<UserUi> = MutableStateFlow(UserUi())
    val user: StateFlow<UserUi> = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getUsersListUseCase().collectLatest { usersResult ->
                usersResult.onSuccess { users ->
                    val currentUser = users.find { user -> user.isLoggedIn }
                    Log.d("TEST", "current user = $currentUser")
                    _user.emit(
                        UserUi(
                            name = currentUser?.name.orEmpty(),
                            id = currentUser?.id ?: -1,
                        )
                    )
                }
            }
        }
    }

    fun likeVideo(userId: Int, videoId: Int) {
        viewModelScope.launch {
            likeVideoUseCase(userId, videoId)
        }
    }
}

data class UserUi(
    val name: String = "",
    val id: Int = 1,
)

data class Video(
    val id: Int,
    val title: String,
    val duration: Double,
    val url: String,
    val userLiked : List<Int>
)

fun VideosList.toUi() = Video(
    id = id,
    title = title,
    duration = duration,
    url = url,
    userLiked = usersLiked
)

fun Double.toStringFormat(): String {
    val totalSeconds = floor(this).toLong()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun List<User>.getLoginUser() = find { it.isLoggedIn }?.name