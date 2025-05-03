package com.saeedev.movieadinterviewtest.presentation.playVideo

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun AdScreen(
    url: String,
    navigateBackToVideos: () -> Unit
) {

    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    val exoPlayer = remember(context, url) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        navigateBackToVideos()
                    }
                }
            })
        }
    }
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

}