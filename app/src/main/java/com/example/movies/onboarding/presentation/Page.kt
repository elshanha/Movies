package com.example.movies.onboarding.presentation


import androidx.annotation.RawRes
import com.example.movies.R

data class Page(
    val title: String,
    val description: String,
    @RawRes val lottie: Int
)

val page = listOf(
    Page(
        title = "List of all movies",
        description = "Check out which movie you want to watch",
        lottie = R.raw.movie_exciting
    ),
    Page(
        title = "Trending and popular movies",
        description = "Sort the movies that you can easily watch",
        lottie = R.raw.movie_latest
    ),
    Page(
        title = "And... Tv shows",
        description = "Don't forget to look at the best tv shows",
        lottie = R.raw.movie_star
    )
)