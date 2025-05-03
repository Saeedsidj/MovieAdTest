package com.saeedev.movieadinterviewtest.presentation.playVideo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedev.movieadinterviewtest.common.onSuccess
import com.saeedev.movieadinterviewtest.di.AdManager
import com.saeedev.movieadinterviewtest.domain.usecase.GetAdvertisesUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.GetUsersListUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.GetVideosListUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.LikeVideoUseCase
import com.saeedev.movieadinterviewtest.presentation.videoList.UserUi
import com.saeedev.movieadinterviewtest.presentation.videoList.Video
import com.saeedev.movieadinterviewtest.presentation.videoList.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVideosListUseCase: GetVideosListUseCase,
    private val getUsersListUseCase: GetUsersListUseCase,
    private val likeVideoUseCase: LikeVideoUseCase,
    private val getAdvertisesUseCase: GetAdvertisesUseCase,
    private val adManager: AdManager
) : ViewModel() {

    private val currentVideoId: MutableState<Int?> =
        mutableStateOf(checkNotNull(savedStateHandle["videoId"]))

    private val _videos: MutableList<Video> = mutableListOf()
    private val _currentVideo: MutableStateFlow<Video?> = MutableStateFlow(null)
    private val _user: MutableStateFlow<UserUi> = MutableStateFlow(UserUi())
    private val next = MutableStateFlow<Int?>(null)
    private val prev = MutableStateFlow<Int?>(null)

    private val _uiState = MutableStateFlow<VideoPlayerUiState?>(null)
    val uiState: StateFlow<VideoPlayerUiState?> = _uiState.asStateFlow()


    init {
        getUser()
        getVideoList()
        setAdUrls()
    }

    private fun setAdUrls() {
        viewModelScope.launch {
            adManager.setAdUrls(getAdvertisesUseCase().map { it.url })
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

    private fun getVideoList() {
        viewModelScope.launch {
            getVideosListUseCase().collectLatest { videos ->
                _videos.addAll(videos.map { it.toUi() })
                val current = _videos.find { it.id == currentVideoId.value }
                _currentVideo.emit(current)
                current?.let {
                    _uiState.emit(
                        VideoPlayerUiState(
                            currentVideo = it,
                            username = _user.value.name,
                            next = null,
                            prev = null,
                            isLiked = it.userLiked.contains(_user.value.id),
                            currentDuration = 0f,
                            showAd = Pair("",-1)
                        )
                    )
                }
            }
        }
    }

    fun updatePlaybackPosition(positionMs: Float) {
        val currentState = _uiState.value ?: return
        _uiState.value = currentState.copy(currentDuration = positionMs)
    }

    fun likeVideo() {
        val userId = _user.value.id
        val videoId = _currentVideo.value?.id ?: -1
        viewModelScope.launch {
            likeVideoUseCase(userId, videoId)
        }
    }

    fun playNext() {
        val next = _videos.dropWhile { it.id != _currentVideo.value?.id }.drop(1).firstOrNull()
        next?.let { next ->
            adManager.recordMainVideoPlayed()
            if (adManager.shouldShowAd()) {
                val adUrl = adManager.getAdUrl()
                _uiState.update {
                    it?.copy(showAd = Pair(adUrl, next.id))
                }
            } else {
                _uiState.update {
                    it?.copy(next = next.id)
                }
            }
        }
    }

    fun playPrev() {
        _currentVideo.value?.let { video ->
            val prev = _videos.takeWhile { it.id != video.id }.lastOrNull()
            prev?.let { prevUrl ->
                _uiState.update {
                    it?.copy(next = prev.id)
                }
            }
        }
    }
}

data class VideoPlayerUiState(
    val currentVideo: Video,
    val username: String,
    val next: Int?,
    val prev: Int?,
    val isLiked: Boolean,
    val currentDuration: Float,
    val showAd: Pair<String, Int>
)