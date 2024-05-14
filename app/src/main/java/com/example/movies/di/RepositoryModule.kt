package com.example.movies.di

import com.ahmedapps.themovies.media_details.data.repository.DetailsRepositoryImpl
import com.ahmedapps.themovies.media_details.data.repository.ExtraDetailsRepositoryImpl
import com.example.movies.media_details.domain.repository.DetailsRepository
import com.example.movies.media_details.domain.repository.ExtraDetailsRepository
import com.example.movies.main.data.repository.GenreRepositoryImpl
import com.example.movies.main.data.repository.MediaRepositoryImpl
import com.example.movies.search.data.repository.SearchRepositoryImpl
import com.example.movies.main.domain.repository.GenreRepository
import com.example.movies.main.domain.repository.MediaRepository
import com.example.movies.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindGenreRepository(
        genreRepositoryImpl: GenreRepositoryImpl
    ): GenreRepository

    @Binds
    @Singleton
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailsRepositoryImpl
    ): DetailsRepository

    @Binds
    @Singleton
    abstract fun bindExtraDetailsRepository(
       extraDetailsRepositoryImpl: ExtraDetailsRepositoryImpl
    ): ExtraDetailsRepository

}
