package com.saeedev.movieadinterviewtest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.saeedev.movieadinterviewtest.presentation.likedVideos.LikedVideosScreen
import com.saeedev.movieadinterviewtest.presentation.playVideo.AdScreen
import com.saeedev.movieadinterviewtest.presentation.playVideo.VideoPlayerScreen
import com.saeedev.movieadinterviewtest.presentation.selectUser.SelectUserBottomSheet
import com.saeedev.movieadinterviewtest.presentation.theme.MovieAdInterviewTestTheme
import com.saeedev.movieadinterviewtest.presentation.videoList.VideosListScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MovieAdInterviewTestTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val bottomSheetNavigator = rememberBottomSheetNavigator()
                        val navController = rememberNavController(bottomSheetNavigator)
                        ModalBottomSheetLayout(bottomSheetNavigator) {
                            NavHost(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                startDestination = Screens.VideosScreen.route
                            ) {
                                composable(
                                    route = Screens.VideosScreen.route
                                ) {
                                    VideosListScreen(
                                        navigateToSelectUSer = {
                                            navController.navigate(Screens.SelectUserBottomSheet.route)
                                        },
                                        navigateToFavorite = {
                                            navController.navigate(
                                                Screens.LikedVideosScreen.createRoute(
                                                    it
                                                )
                                            )
                                        },
                                        onPlayClick = {
                                            navController.navigate(
                                                Screens.PlayVideoScreen.createRoute(
                                                    it
                                                )
                                            )
                                        }
                                    )
                                }
                                bottomSheet(
                                    route = Screens.SelectUserBottomSheet.route
                                ) {
                                    SelectUserBottomSheet(
                                        onNavigateBack = navController::navigateUp
                                    )
                                }
                                composable(
                                    route = Screens.LikedVideosScreen.route,
                                    arguments = listOf(navArgument("userId") {
                                        type = NavType.IntType
                                    })
                                ) {
                                    LikedVideosScreen(
                                        navigateBack = navController::navigateUp
                                    )
                                }
                                composable(
                                    route = Screens.PlayVideoScreen.route,
                                    arguments = listOf(navArgument("videoId") {
                                        type = NavType.IntType
                                    })
                                ) {
                                    VideoPlayerScreen(
                                        navigateToOtherVideo = {
                                            navController.navigate(
                                                Screens.PlayVideoScreen.createRoute(it)
                                            ) {
                                                popUpTo(Screens.VideosScreen.route) {
                                                    inclusive = false
                                                }
                                            }
                                        },
                                        navigateToAdVideo = { adUrl, nextVideoId ->
                                            navController.navigate(
                                                Screens.PlayAdScreen.createRoute(adUrl, nextVideoId)
                                            ) {
                                                popUpTo(Screens.VideosScreen.route) {
                                                    inclusive = false
                                                }
                                            }
                                        }
                                    )
                                }
                                composable(
                                    route = Screens.PlayAdScreen.route,
                                    arguments = listOf(
                                        navArgument("adUrl") {
                                            type = NavType.StringType
                                            nullable = false
                                        },
                                        navArgument("nextVideo") {
                                            type = NavType.IntType
                                            defaultValue = -1
                                        }
                                    )
                                ) { backStackEntry ->
                                    val adUrl = backStackEntry.arguments?.getString("adUrl") ?: ""
                                    val nextVideo =
                                        backStackEntry.arguments?.getInt("nextVideo") ?: -1
                                    AdScreen(adUrl) {
                                        navController.navigate(
                                            Screens.PlayVideoScreen.createRoute(nextVideo)
                                        ) {
                                            popUpTo(Screens.VideosScreen.route) {
                                                inclusive = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieAdInterviewTestTheme {
        Greeting("Android")
    }
}