package com.example.movies.main.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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
import com.example.movies.login.profile.ProfileScreen
import com.example.movies.login.sign_in.GoogleAuthUiClient
import com.example.movies.login.sign_in.SignInScreen
import com.example.movies.login.sign_in.SignInViewModel
import com.example.movies.media_details.presentation.details.MediaDetailScreen
import com.example.movies.media_details.presentation.details.MediaDetailsScreenEvents
import com.example.movies.media_details.presentation.details.MediaDetailsViewModel
import com.example.movies.media_details.presentation.details.SomethingWentWrong
import com.example.movies.media_details.presentation.similar_media.SimilarMediaListScreen
import com.example.movies.media_details.presentation.watch_video.WatchVideoScreen
import com.example.movies.navigation.Navigation
import com.example.movies.navigation.Route
import com.example.movies.notifications.NotificationsService
import com.example.movies.notifications.biometric.BiometricPromptManager
import com.example.movies.onboarding.presentation.OnBoardingScreen
import com.example.movies.onboarding.presentation.OnboardingViewModel
import com.example.movies.search.presentation.SearchScreen
import com.example.movies.ui.theme.MoviesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = { mainViewModel.showSplashScreen.value })
        }
        setContent {
            MoviesTheme(
                dynamicColor = false
            ) {
                Scaffold {
                    val paddingValues = it

                    val mainUiState = mainViewModel.mainUiState.collectAsState().value

                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    val isSystemInDarkMode = isSystemInDarkTheme()
                    val systemUiColor = rememberSystemUiController()
                    SideEffect {
                        systemUiColor.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = !isSystemInDarkMode
                        )
                    }
                    val context = LocalContext.current
                    val biometricResult by promptManager.promptResults.collectAsState(
                        initial = null
                    )

                    val enrollLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = {
                            println("Activity result: $it")
                        }
                    )
                    LaunchedEffect(biometricResult) {
                        if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
                            if (Build.VERSION.SDK_INT >= 30) {
                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                    putExtra(
                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                    )
                                }
                                enrollLauncher.launch(enrollIntent)
                            }
                        }
                    }

                    LaunchedEffect(key1 = mainUiState.biometricAuthEnabled) {
                        mainViewModel.loadBiometricsEnabled()
                        if (mainUiState.biometricAuthEnabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                promptManager.showBiometricPrompt(
                                    title = "Biometric Authentication",
                                    description = "Login using your biometric credential"
                                )
                            }
                        }

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionState =
                            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                        if (!permissionState.status.isGranted) {
                            LaunchedEffect(key1 = Unit) {
                                permissionState.launchPermissionRequest()
                                mainViewModel.createNotificationChannel()
                            }
                        } else {
                            Unit
                        }

                    }

                    Box(
                        Modifier
                            .statusBarsPadding()
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Navigation(
                            context = this@MainActivity,
                            mainUiState = mainUiState,
                            onEvent = mainViewModel::onEvent,
                            startDestination = mainViewModel.startDestination.value,
                            lifecycleOwner = this@MainActivity,
                            googleAuthUiClient = googleAuthUiClient,
                            promptManager = promptManager
                        )

                    }
                }
            }
        }
    }
}












