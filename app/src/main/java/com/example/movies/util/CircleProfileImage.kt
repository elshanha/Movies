package com.example.movies.util

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movies.login.sign_in.UserData
import com.example.movies.navigation.Route

@Composable
fun CircleProfileImage(
    modifier: Modifier = Modifier,
    userData: UserData?,
    navController: NavController
) {
    if(userData?.profilePictureUrl != null) {
        AsyncImage(
            model = userData.profilePictureUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .clickable {
                    navController.navigate(Route.PROFILE)
                }
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(2.dp,  MaterialTheme.colorScheme.secondaryContainer, CircleShape),
            contentScale = ContentScale.Crop
        )
    }

}