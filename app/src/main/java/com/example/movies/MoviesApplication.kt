package com.example.movies

import android.app.Application
import androidx.activity.viewModels
import com.example.movies.main.presentation.main.MainViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApplication : Application(
) {

    override fun onCreate() {
        super.onCreate()

    }

}