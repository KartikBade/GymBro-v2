package com.example.gymbro_v2.database

import androidx.room.*
import com.example.gymbro_v2.model.User

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUser(email: String): User
}