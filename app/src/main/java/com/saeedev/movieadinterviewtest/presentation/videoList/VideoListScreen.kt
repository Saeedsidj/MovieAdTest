package com.saeedev.movieadinterviewtest.presentation.videoList

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.saeedev.movieadinterviewtest.R

@Composable
fun VideosListScreen(
    viewModel: VideoListViewModel = hiltViewModel(),
    navigateToSelectUSer: () -> Unit,
    navigateToFavorite: (Int) -> Unit,
    onPlayClick: (Int) -> Unit
) {
    val user by viewModel.user.collectAsState()
    val videos = viewModel.videos.collectAsLazyPagingItems()

    VideoListScreen(
        user = user,
        videos = videos,
        onLikeClick = { videoId ->
            viewModel.likeVideo(user.id, videoId)
        },
        navigateToSelectUSer = navigateToSelectUSer,
        navigateToFavorite = navigateToFavorite,
        onPlayClick = onPlayClick
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VideoListScreen(
    user: UserUi,
    videos: LazyPagingItems<Video>,
    onLikeClick: (videoId: Int) -> Unit,
    navigateToSelectUSer: () -> Unit,
    navigateToFavorite: (Int) -> Unit,
    onPlayClick: (Int) -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color(44, 37, 47), Color.Black)))
                .fillMaxSize()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileCard(
                    user = user.name,
                    onCardClicked = navigateToSelectUSer
                )
                LikedCard(
                    onCardClicked = { navigateToFavorite(user.id) }
                )
            }

            LazyColumn {
                items(videos.itemCount) { index ->
                    videos[index]?.let {
                        VideoCard(
                            username = user.name,
                            url = it.url,
                            duration = it.duration.toStringFormat(),
                            description = it.title,
                            isLiked = videos[index]?.userLiked?.contains(user.id) ?: false,
                            onLikeClick = { onLikeClick(videos[index]?.id ?: 1) },
                            onPlayClick = {
                                onPlayClick(it.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    url: String,
    duration: String = "00:49",
    description: String = "لورم ایپسوم متن ساختگی",
    username: String = "کاربر اول",
    isLiked: Boolean,
    onPlayClick: () -> Unit = {},
    onLikeClick: () -> Unit = {}
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(VideoFrameDecoder.Factory()) }
        .build()
    Box(
        modifier = modifier
            .padding(bottom = 22.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onPlayClick,
            modifier = Modifier
                .size(58.dp)
                .align(Alignment.Center)
                .background(Color(0xAA000000), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 12.dp, end = 12.dp)
        ) {
            Text(
                text = "مدت زمان ویدیو: $duration",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = description,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    user: String = "alex",
    onCardClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onCardClicked)
            .background(Color(red = 47, green = 37, blue = 51))
            .border(1.dp, Color(red = 97, green = 71, blue = 115), CircleShape)
            .padding(vertical = 8.5.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            contentDescription = "change user clickable",
            painter = painterResource(R.drawable.user_icon),
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(start = 9.dp),
            text = user,
            color = Color.White,
        )
        Icon(
            modifier = Modifier.padding(start = 17.dp),
            contentDescription = "",
            painter = painterResource(R.drawable.expand_less),
            tint = Color.White
        )
    }
}

@Composable
fun LikedCard(
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onCardClicked)
            .background(Color(red = 47, green = 37, blue = 51))
            .border(1.dp, Color(red = 97, green = 71, blue = 115), CircleShape)
            .padding(vertical = 8.5.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.liked_videos),
            color = Color.White,
        )
        Icon(
            modifier = Modifier.padding(start = 10.dp),
            contentDescription = "",
            painter = painterResource(R.drawable.liked_icon),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun Preview(modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxSize()
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileCard()
            LikedCard()
        }
    }
}


@Composable
fun NetworkError(modifier: Modifier = Modifier) {

}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Color(red = 47, green = 37, blue = 51)
        )
    }
}