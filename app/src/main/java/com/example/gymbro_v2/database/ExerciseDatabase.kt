package com.example.gymbro_v2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymbro_v2.model.Exercise

@Database([Exercise::class], version = 1)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract fun getDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            return (INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exerciseDb"
                ).build()
                INSTANCE = instance

                instance
            })
        }
    }
}