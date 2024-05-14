package com.example.movies.navigation

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.movies.favorites.FavoritesScreen
import com.example.movies.login.profile.ProfileScreen
import com.example.movies.login.sign_in.GoogleAuthUiClient
import com.example.movies.login.sign_in.SignInScreen
import com.example.movies.login.sign_in.SignInViewModel
import com.example.movies.main.presentation.main.MainActivity
import com.example.movies.main.presentation.main.MainUiEvents
import com.example.movies.main.presentation.main.MainUiState
import com.example.movies.main.presentation.main.MainViewModel
import com.example.movies.main.presentation.main.MediaMainScreen
import com.example.movies.media_details.presentation.details.MediaDetailScreen
import com.example.movies.media_details.presentation.details.MediaDetailsScreenEvents
import com.example.movies.media_details.presentation.details.MediaDetailsViewModel
import com.example.movies.media_details.presentation.details.SomethingWentWrong
import com.example.movies.media_details.presentation.similar_media.SimilarMediaListScreen
import com.example.movies.media_details.presentation.watch_video.WatchVideoScreen
import com.example.movies.notifications.NotificationsService
import com.example.movies.notifications.biometric.BiometricPromptManager
import com.example.movies.notifications.biometric.BiometricScreen
import com.example.movies.onboarding.presentation.OnBoardingScreen
import com.example.movies.onboarding.presentation.OnboardingViewModel
import com.example.movies.search.presentation.SearchScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    context: MainActivity,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleOwner: MainActivity,
    // notificationsService: NotificationsService,
    promptManager: BiometricPromptManager
) {
    val navController = rememberNavController()

    val mainViewModel = hiltViewModel<MainViewModel>()

    val mediaDetailsViewModel = hiltViewModel<MediaDetailsViewModel>()
    val mediaDetailsScreenState =
        mediaDetailsViewModel.mediaDetailsScreenState.collectAsState().value

    val biometricResult by promptManager.promptResults.collectAsState(
        initial = null
    )

    NavHost(

        navController = navController,
        startDestination = startDestination
        // startDestination = Route.MEDIA_MAIN_SCREEN
    ) {
        navigation(
            route = Route.APP_START_NAVIGATION,
            startDestination = Route.ONBOARDING
        ) {
            composable(route = Route.ONBOARDING) {
                val viewModel: OnboardingViewModel = hiltViewModel()
                OnBoardingScreen(onEvent = viewModel::onEvent)
            }
        }

        navigation(
            route = Route.MOVIES_NAVIGATION,
            startDestination =
            if (googleAuthUiClient.getSignedInUser() != null) {
                if (mainUiState.biometricAuthEnabled) {
                    Route.MEDIA_MAIN_SCREEN
                } else {
                    Route.MEDIA_MAIN_SCREEN
                }
            } else {
                Route.SIGNIN
            }
        ) {

            composable(Route.SIGNIN, enterTransition = { scaleIn() + fadeIn() }) {
                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(Route.MEDIA_MAIN_SCREEN)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            lifecycleOwner.lifecycleScope.launch {
                                val signInResult =
                                    googleAuthUiClient.signInWithIntent(
                                        intent = result.data
                                            ?: return@launch
                                    )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            context,
                            "Sign in successful",
                            Toast.LENGTH_LONG
                        ).show()
                        mainViewModel.showLikeNotification("Welcome back ${googleAuthUiClient.getSignedInUser()?.username}!")

                        // notificationsService.showNotification("Welcome back ${googleAuthUiClient.getSignedInUser()?.username}!")

                        navController.navigate(Route.MEDIA_MAIN_SCREEN)
                        viewModel.resetState()
                    }
                }

                SignInScreen(
                    state = state,
                    onSignInClick = {
                        lifecycleOwner.lifecycleScope.launch {
                            val signInIntentSender =
                                googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )

            }

            composable(Route.PROFILE, enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 200,
                    )
                )
            },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = 200,
                        )
                    )
                }
            ) {
                ProfileScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        lifecycleOwner.lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                context,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.navigate(Route.SIGNIN)
                        }
                    },
                    mainUiState = mainUiState,
                    mainViewModel = mainViewModel
                )
            }


            composable(Route.MEDIA_MAIN_SCREEN) {
                MediaMainScreen(
                    navController = navController,
                    mainUiState = mainUiState,
                    onEvent = onEvent,
                    googleAuthUiClient = googleAuthUiClient
                )
            }

            composable(Route.SEARCH_SCREEN) {
                SearchScreen(
                    navController = navController,
                    mainUiState = mainUiState,
                )
            }

            composable(
                "${Route.MEDIA_DETAILS_SCREEN}?id={id}&type={type}&category={category}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("type") { type = NavType.StringType },
                    navArgument("category") { type = NavType.StringType }
                )
            ) {

                val id = it.arguments?.getInt("id") ?: 0
                val type = it.arguments?.getString("type") ?: ""
                val category = it.arguments?.getString("category") ?: ""

                LaunchedEffect(key1 = true) {
                    mediaDetailsViewModel.onEvent(
                        MediaDetailsScreenEvents.SetDataAndLoad(
                            moviesGenresList = mainUiState.moviesGenresList,
                            tvGenresList = mainUiState.tvGenresList,
                            id = id,
                            type = type,
                            category = category
                        )
                    )
                }

                if (mediaDetailsScreenState.media != null) {
                    MediaDetailScreen(
                        navController = navController,
                        media = mediaDetailsScreenState.media,
                        mediaDetailsScreenState = mediaDetailsScreenState,
                        onEvent = mediaDetailsViewModel::onEvent
                    )
                } else {
                    SomethingWentWrong()
                }
            }

            composable(
                "${Route.SIMILAR_MEDIA_LIST_SCREEN}?title={title}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                )
            ) {

                val name = it.arguments?.getString("title") ?: ""

                SimilarMediaListScreen(
                    navController = navController,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    name = name,
                )
            }

//            composable(
//                "${Route.WATCH_VIDEO_SCREEN}?videoId={videoId}",
//                arguments = listOf(
//                    navArgument("videoId") { type = NavType.StringType }
//                )
//            ) {
//
//                val videoId = it.arguments?.getString("videoId") ?: ""
//
//                WatchVideoScreen(
//                    lifecycleOwner = LocalLifecycleOwner.current,
//                    videoId = videoId
//                )
//            }

            composable(Route.FAVORITES) {
                FavoritesScreen(
                    navController = navController,

                    )

            }

            composable(Route.BIOMETRIC) {

                biometricResult.let {
                    if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
                        navController.navigate(Route.MEDIA_MAIN_SCREEN)
                    }
                }


                BiometricScreen(navController = navController)
            }
        }
    }
}
