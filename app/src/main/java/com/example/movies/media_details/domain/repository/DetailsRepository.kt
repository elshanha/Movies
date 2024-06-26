package com.example.movies.media_details.domain.repository

import com.example.movies.main.domain.models.Media
import com.example.movies.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>>

}










