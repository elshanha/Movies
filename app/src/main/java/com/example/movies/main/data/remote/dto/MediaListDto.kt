package com.example.movies.main.data.remote.dto

import com.example.movies.main.data.remote.dto.MediaDto

data class MediaListDto(
    val page: Int,
    val results: List<MediaDto>,
    val total_pages: Int,
    val total_results: Int
)