package com.saeedev.movieadinterviewtest.presentation.likedVideos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.saeedev.movieadinterviewtest.common.onSuccess
import com.saeedev.movieadinterviewtest.domain.usecase.GetUsersListUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.GetPaginatedVideosUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.LikeVideoUseCase
import com.saeedev.movieadinterviewtest.presentation.videoList.UserUi
import com.saeedev.movieadinterviewtest.presentation.videoList.Video
import com.saeedev.movieadinterviewtest.presentation.videoList.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedVideosViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUsersListUseCase: GetUsersListUseCase,
    private val getVideosListUseCase: GetPaginatedVideosUseCase,
    private val likeVideoUseCase: LikeVideoUseCase
) : ViewModel() {

    private val _videos: MutableStateFlow<PagingData<Video>> = MutableStateFlow(PagingData.empty())
    val videos: StateFlow<PagingData<Video>> = _videos.asStateFlow()

    private val _user: MutableStateFlow<UserUi> = MutableStateFlow(UserUi())
    val user: StateFlow<UserUi> = _user.asStateFlow()

    private val userId: Int = checkNotNull(savedStateHandle["userId"])

    init {
        getVideoList()
        getUser()
    }

    private fun getVideoList() {
        viewModelScope.launch {
            getVideosListUseCase().collectLatest { videos ->
                    _videos.emit(videos.map { it.toUi() }.filter { it.userLiked.contains(userId) })
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            getUsersListUseCase().collectLatest { usersResult ->
                usersResult.onSuccess { users ->
                    val currentUser = users.find { user -> user.isLoggedIn }
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