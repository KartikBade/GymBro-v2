package com.example.gymbro_v2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymbro_v2.model.User

@Database([User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return (INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "inventoryDb"
                ).build()
                INSTANCE = instance

                instance
            })
        }
    }
}