package com.example.gymbro_v2.di

import android.app.Application
import com.example.gymbro_v2.database.ExerciseDatabase
import com.example.gymbro_v2.repository.AuthRepository
import com.example.gymbro_v2.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        return AuthRepository(
            context,
            FirebaseAuth.getInstance(),
            Firebase.firestore
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(context: Application): UserRepository {
        return UserRepository(
            context,
            FirebaseAuth.getInstance(),
            ExerciseDatabase.getDatabase(context)
        )
    }
}