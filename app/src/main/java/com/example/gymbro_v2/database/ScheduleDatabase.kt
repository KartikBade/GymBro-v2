package com.example.gymbro_v2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymbro_v2.model.Schedule

@Database([Schedule::class], version = 1)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun getDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        fun getDatabase(context: Context): ScheduleDatabase {
            return (INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "exerciseDb"
                ).build()
                INSTANCE = instance

                instance
            })
        }
    }
}