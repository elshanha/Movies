package com.example.movies.main.data.remote.dto

import com.example.movies.main.domain.models.Genre

data class GenresListDto(
    val genres: List<Genre>
)