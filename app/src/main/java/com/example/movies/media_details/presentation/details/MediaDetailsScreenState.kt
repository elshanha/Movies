package com.example.movies.media_details.presentation.details

import com.example.movies.main.domain.models.Genre
import com.example.movies.main.domain.models.Media

data class MediaDetailsScreenState(

    val isLoading: Boolean = false,

    val media: Media? = null,

    val videoId: String = "",
    val readableTime: String = "",

    val similarMediaList: List<Media> = emptyList(),
    val smallSimilarMediaList: List<Media> = emptyList(),

    val videosList: List<String> = emptyList(),
    val moviesGenresList: List<Genre> = emptyList(),
    val tvGenresList: List<Genre> = emptyList()

)