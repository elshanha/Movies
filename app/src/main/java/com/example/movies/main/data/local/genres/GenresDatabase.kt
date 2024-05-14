package com.example.movies.main.data.local.genres

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.main.data.local.genres.GenreDao
import com.example.movies.main.data.local.genres.GenreEntity

@Database(
    entities = [GenreEntity::class],
    version = 1
)
abstract class GenresDatabase: RoomDatabase() {
    abstract val genreDao: GenreDao
}