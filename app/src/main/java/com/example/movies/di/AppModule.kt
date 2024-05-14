package com.example.movies.di

import android.app.Application
import androidx.room.Room
import com.example.movies.media_details.data.remote.api.ExtraDetailsApi
import com.example.movies.main.data.local.genres.GenresDatabase
import com.example.movies.main.data.local.media.MediaDatabase
import com.example.movies.main.data.remote.api.GenresApi
import com.example.movies.main.data.remote.api.MediaApi
import com.example.movies.main.data.remote.api.MediaApi.Companion.BASE_URL
import com.example.movies.notifications.NotificationsService
import com.example.movies.notifications.NotificationsServiceImpl
import com.example.movies.onboarding.data.LocalUserMangerImpl
import com.example.movies.onboarding.domain.LocalUserManger
import com.example.movies.onboarding.domain.appentry.AppEntryUseCases
import com.example.movies.onboarding.domain.appentry.ReadAppEntry
import com.example.movies.onboarding.domain.appentry.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGenreDatabase(app: Application): GenresDatabase {
        return Room.databaseBuilder(
            app,
            GenresDatabase::class.java,
            "genresdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaDatabase(app: Application): MediaDatabase {
        return Room.databaseBuilder(
            app,
            MediaDatabase::class.java,
            "mediadb.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMoviesApi(): MediaApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MediaApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGenresApi(): GenresApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(GenresApi::class.java)
    }

    @Singleton
    @Provides
    fun provideExtraDetailsApi(): ExtraDetailsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ExtraDetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideNotificationManager(
        application: Application
    ): NotificationsService = NotificationsServiceImpl(context = application)

}









