package com.example.movies.notifications.biometric

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.movies.navigation.Route

@Composable
fun BiometricScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {



//    LaunchedEffect(key1 = Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            promptManager.showBiometricPrompt(
//                title = "Sample prompt",
//                description = "Sample prompt description"
//            )
//
//        }
//    }
//                        val biometricResult by promptManager.promptResults.collectAsState(
//                        initial = null
//                    )
//                    val enrollLauncher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.StartActivityForResult(),
//                        onResult = {
//                            println("Activity result: $it")
//                        }
//                    )
//                    LaunchedEffect(biometricResult) {
//                        if(biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
//                            if(Build.VERSION.SDK_INT >= 30) {
//                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                                    putExtra(
//                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
//                                    )
//                                }
//                                enrollLauncher.launch(enrollIntent)
//                            }
//                        }
//                    }
//
//                    LaunchedEffect(key1 = mainViewModel.biometricPromptEnabled) {
//                        promptManager.showBiometricPrompt(
//                            title = "Sample prompt",
//                            description = "Sample prompt description"
//                        )
//                    }



    Box(
        modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {



    }

}