package com.example.movies.util.ui_shared_components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.movies.ui.theme.font
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.movies.main.presentation.main.MainUiEvents
import com.example.movies.main.presentation.main.MainUiState
import com.example.movies.R
import com.example.movies.main.data.remote.api.MediaApi.Companion.IMAGE_BASE_URL
import com.example.movies.main.domain.models.Media
import com.example.movies.ui.theme.Radius
import com.example.movies.ui.theme.RadiusContainer
import com.example.movies.util.Constants
import com.example.movies.navigation.Route
import com.example.movies.notifications.NotificationsService
import com.example.movies.search.presentation.SearchUiEvents
import com.example.movies.util.getAverageColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaItem(
    media: Media,
    navController: NavController,
    mainUiState: MainUiState,
    modifier: Modifier = Modifier,
    onEvent: (MainUiEvents) -> Unit,
) {


    var isLongPressed by remember { mutableStateOf(mainUiState.showHeart) }

//    if (isLongPressed) {
//        AnimatedHeart(isLongPressed)
//    }


    val imageUrl = "${IMAGE_BASE_URL}${media.posterPath}"

    val title = media.title

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )
    val imageState = imagePainter.state

    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier.padding(
            bottom = 16.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(RadiusContainer.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            dominantColor
                        )
                    )
                )
                .combinedClickable(
                    onClick = {
                        navController.navigate(
                            "${Route.MEDIA_DETAILS_SCREEN}?id=${media.id}&type=${media.mediaType}&category=${media.category}"
                        )
                    },
                    onLongClick = {
                        mainUiState.showNotification = true
                      //  isLongPressed = !isLongPressed
                        mainUiState.showHeart = !mainUiState.showHeart
                        onEvent(MainUiEvents.AddToFavorites(" $title added to favorites!"))
                    }
                )
        ) {

            Box(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxSize()
                    .padding(6.dp)
            ) {

                if (imageState is AsyncImagePainter.State.Success) {

                    val imageBitmap = imageState.result.drawable.toBitmap()


                    dominantColor = getAverageColor(imageBitmap.asImageBitmap())

                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(Radius.dp))
                            .background(MaterialTheme.colorScheme.background),
                    )

                }

                if (imageState is AsyncImagePainter.State.Error) {
                    dominantColor = MaterialTheme.colorScheme.primary
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(Radius.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(32.dp)
                            .alpha(0.8f),
                        painter = painterResource(id = R.drawable.ic_no_image),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }


                if (imageState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                            .scale(0.5f)
                    )
                }
            }

            var badgeCount by remember { mutableIntStateOf(0) }
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                text = title,
                fontFamily = font,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = title
                            .substring(lineEndIndex)
                            .count { it == '.' }
                    }
                },
            )

            val genres = genresProvider(
                genre_ids = media.genreIds,
                allGenres = if (media.mediaType == Constants.MOVIE)
                    mainUiState.moviesGenresList
                else mainUiState.tvGenresList
            )

            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp
                    ),
                text = genres,
                fontFamily = font,
                fontSize = 12.5.sp,
                maxLines = 1,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = genres
                            .substring(lineEndIndex)
                            .count { it == '.' }
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        start = 11.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        modifier = Modifier,
                        starsModifier = Modifier
                            .size(18.dp),
                        rating = media.voteAverage / 2
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 4.dp
                            ),
                        text = media.voteAverage.toString().take(3),
                        fontFamily = font,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color.LightGray
                    )
                }

            }
        }
    }
}


