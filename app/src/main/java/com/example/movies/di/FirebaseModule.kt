package com.example.movies.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

//
//    @Provides
//    @Singleton
//    fun providesAuthRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
//        return AuthRepositoryImplementation(firebaseAuth = firebaseAuth)
//    }




//    @Provides
//    @Singleton
//    fun providesDatabaseRepositoryImpl(): DatabaseRepository {
//        return DatabaseRepositoryImpl()
//    }

}