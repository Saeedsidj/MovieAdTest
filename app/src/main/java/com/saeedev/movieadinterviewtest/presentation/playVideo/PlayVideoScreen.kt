package com.saeedev.movieadinterviewtest.presentation.playVideo

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
import androidx.media3.ui.PlayerView
import com.saeedev.movieadinterviewtest.R
import retrofit2.http.Url

@Composable
fun VideoPlayerScreen(
    viewModel: VideoPlayerViewModel = hiltViewModel(),
    navigateToOtherVideo: (Int) -> Unit,
    navigateToAdVideo: (adUrl: String, nextMovieUrl: Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    uiState?.let {

        val context = LocalContext.current
        var isPlaying by remember { mutableStateOf(true) }
        val exoPlayer = remember(context, it.currentVideo.url) {
            ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(Uri.parse(it.currentVideo.url))
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            viewModel.playNext()
                        }
                    }
                })
            }
        }
        DisposableEffect(it.prev) {
            it.prev?.let(navigateToOtherVideo)
            onDispose {
                exoPlayer.release()
            }
        }
        DisposableEffect(it.showAd) {
            if (it.showAd.first.isNotEmpty()) {
                navigateToAdVideo(it.showAd.first,it.showAd.second)
            }
            onDispose {
                exoPlayer.release()
            }
        }
        DisposableEffect(it.next) {
            it.next?.let(navigateToOtherVideo)
            onDispose {
                exoPlayer.release()
            }
        }
        DisposableEffect(Unit) {
            onDispose { exoPlayer.release() }
        }

        VideoPlayerScreenBase(
            exoPlayer = exoPlayer,
            videoTitle = it.currentVideo.title,
            userName = it.username,
            isLiked = it.isLiked,
            isPlaying = false,
            videoDuration = it.currentVideo.duration.toFloat(),
            currentPosition = it.currentDuration,
            onPlayPauseClick = {
                isPlaying = !isPlaying
                exoPlayer.playWhenReady = isPlaying
            },
            onNextClick = {
                viewModel.playNext()
                exoPlayer.release()
                exoPlayer.prepare()
            },
            onPrevClick = {
                viewModel.playPrev()
                exoPlayer.release()
                exoPlayer.prepare()
            },
            onLikeClick = viewModel::likeVideo,
            onSliderValueChange = { duration ->
                exoPlayer.seekTo((duration * 1000).toLong())
                viewModel.updatePlaybackPosition(duration)
            }
        )
    }

}


@OptIn(UnstableApi::class) // yes i know but...
@Composable
fun VideoPlayerScreenBase(
    exoPlayer: ExoPlayer,
    videoTitle: String,
    userName: String,
    videoDuration: Float,
    currentPosition: Float,
    isPlaying: Boolean,
    isLiked: Boolean,
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onPrevClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onSliderValueChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = exoPlayer
                    this.useController = false
                    this.resizeMode = RESIZE_MODE_FIXED_HEIGHT
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNextClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Previous",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFB240FF), CircleShape)
            ) {
                Icon(
                    painter = if (isPlaying) painterResource(R.drawable.play_icon) else painterResource(
                        R.drawable.pause_icon
                    ),
                    contentDescription = "Play",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = onPrevClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = videoTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Magenta,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = userName,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier
                        .size(43.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White
                    )
                }
            }
            Slider(
                value = currentPosition,
                valueRange = 0f..videoDuration,
                onValueChange = onSliderValueChange
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentPosition.toString(),
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = videoDuration.toString(),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}