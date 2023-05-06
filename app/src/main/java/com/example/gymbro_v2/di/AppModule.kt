package com.example.gymbro_v2.di

import android.app.Application
import android.content.SharedPreferences
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.UserDatabase
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
            Firebase.firestore,
            context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0))
    }

    @Provides
    @Singleton
    fun provideUserRepository(context: Application): UserRepository {
        return UserRepository(
            context,
            )
    }
}