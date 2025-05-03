package com.saeedev.movieadinterviewtest.presentation


sealed class Screens(val route: String) {
    object VideosScreen : Screens("videos_list_screen")
    object SelectUserBottomSheet : Screens("select_user")
    object LikedVideosScreen : Screens("user_liked_videos/{userId}") {
        fun createRoute(userId: Int) = "user_liked_videos/$userId"
    }

    object PlayVideoScreen : Screens("play_video/{videoId}") {
        fun createRoute(videoId: Int) = "play_video/$videoId"
    }

    object PlayAdScreen : Screens("play_ad?adUrl={adUrl}&nextVideo={nextVideo}") {
        fun createRoute(adUrl: String, nextVideo: Int): String {
            return "play_ad?adUrl=$adUrl&nextVideo=$nextVideo"
        }
    }
}