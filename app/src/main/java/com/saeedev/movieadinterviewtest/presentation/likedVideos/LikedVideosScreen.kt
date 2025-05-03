package com.saeedev.movieadinterviewtest.presentation.likedVideos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.saeedev.movieadinterviewtest.R
import com.saeedev.movieadinterviewtest.presentation.videoList.VideoCard
import com.saeedev.movieadinterviewtest.presentation.videoList.toStringFormat

@Composable
fun LikedVideosScreen(
    likedVideosViewModel: LikedVideosViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val videos = likedVideosViewModel.videos.collectAsLazyPagingItems()
    val user by likedVideosViewModel.user.collectAsState()
    Column(
        modifier = Modifier
            .background(Brush.linearGradient(listOf(Color(44, 37, 47), Color.Black))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            onBackClick = navigateBack
        )
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items(videos.itemCount) { index ->
                videos[index]?.let {
                    VideoCard(
                        username = user.name,
                        url = it.url,
                        duration = it.duration.toStringFormat(),
                        description = it.title,
                        isLiked = videos[index]?.userLiked?.contains(user.id) ?: false,
                        onLikeClick = {
                            likedVideosViewModel.likeVideo(
                                user.id,
                                videos[index]?.id ?: 1
                            )
                        }
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun TopBar(
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 22.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.favorite),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Color.White,
                contentDescription = ""
            )
        }
    }
}