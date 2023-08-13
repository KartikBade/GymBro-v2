package com.example.gymbro_v2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef

@Database(
    entities = [
        Schedule::class,
        Exercise::class,
        ScheduleExerciseCrossRef::class,
//        Log::class
               ],
    version = 1
)
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