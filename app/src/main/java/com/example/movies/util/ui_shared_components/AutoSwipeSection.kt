package com.example.movies.util.ui_shared_components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movies.main.presentation.main.MainUiState
import com.example.movies.ui.theme.font

@Composable
fun AutoSwipeSection(
    type: String,
    navController: NavController,
    mainUiState: MainUiState,
) {

    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.outline,
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "",
    )
    
//    val fontSize by infiniteTransition.animateFloat(
//        initialValue = 20.sp.value,
//        targetValue = 25.sp.value,
//        animationSpec = infiniteRepeatable(
//            animation = keyframes {
//                durationMillis = 1000
//                20.sp.value at 0 using FastOutLinearInEasing
//                23.sp.value at 500 using FastOutLinearInEasing
//                25.sp.value at 1000 using FastOutLinearInEasing
//            }
//        ),
//        label = ""
//    )
    

    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type,
                color = color,
                fontFamily = font,
                fontSize = 20.sp
            )
        }

        AutoSwipeImagePager(
            mediaList = mainUiState.specialList.take(7),
            navController = navController,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(0.9f)
        )
    }

}
