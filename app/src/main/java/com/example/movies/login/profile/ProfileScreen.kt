package com.example.movies.login.profile

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movies.login.sign_in.MyButton
import com.example.movies.login.sign_in.UserData
import com.example.movies.main.presentation.main.MainUiState
import com.example.movies.main.presentation.main.MainViewModel
import com.example.movies.navigation.Route

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    mainUiState: MainUiState,
    mainViewModel: MainViewModel,
) {


    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        var biometricEnabled by remember { mutableStateOf(mainUiState.biometricAuthEnabled) }
        var notificationsEnabled by remember { mutableStateOf(mainUiState.showNotification) }

        SettingsItem(
            text = if (biometricEnabled) "Biometric Enabled" else "Biometric Disabled",
            enabled = biometricEnabled,
            onCheckedChange = {
                biometricEnabled = it
                mainViewModel.updateBiometricsEnabled(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsItem(
            text = "Notifications",
            enabled = notificationsEnabled,
            onCheckedChange = {
                notificationsEnabled = it
                mainViewModel.hideNotification()
            })



        MyButton(
            modifier = Modifier.navigationBarsPadding(),
            onClick = onSignOut, text = "Sign Out",
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )

    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    onCheckedChange: ((Boolean) -> Unit)
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Normal,
            text = text
        )

        Switch(
            modifier = Modifier
                .padding(end = 20.dp),
            checked = enabled, onCheckedChange = onCheckedChange
        )
    }

}

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//
//            Text(
//                modifier = Modifier.padding(start = 20.dp),
//                text = if (biometricEnabled) "Biometric Enabled" else "Biometric Disabled",
//                fontSize = 23.sp,
//                fontWeight = FontWeight.Normal
//            )
//
//            Switch(
//                modifier = Modifier
//                    .padding(end = 20.dp),
//                checked = biometricEnabled,
//                onCheckedChange = {
//                    biometricEnabled = it
//                    mainViewModel.updateBiometricsEnabled(it)
//                    //   saveBiometricPreference(context, it)
//                })
//        }
