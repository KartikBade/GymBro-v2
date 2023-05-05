package com.example.gymbro_v2.di

import android.app.Application
import com.example.gymbro_v2.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(context: Application): AuthRepository {
        return AuthRepository(context, FirebaseAuth.getInstance())
    }
}