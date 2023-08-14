package com.example.gymbro_v2.repository

import android.content.Context
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.ScheduleDatabase
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.google.firebase.auth.FirebaseAuth

class UserRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val scheduleDatabase: ScheduleDatabase
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    suspend fun logout() {
        firebaseAuth.signOut()
        userSharedPref.edit().remove(context.getString(R.string.user_shared_pref_username)).apply()
        scheduleDatabase.getDao().deleteAllSchedules()
    }

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleDatabase.getDao().insertSchedule(schedule)
    }

    fun getAllSchedules() = scheduleDatabase.getDao().getAllSchedules()

    suspend fun deleteSchedule(scheduleId: Int) {
        scheduleDatabase.getDao().deleteSchedule(scheduleId)
    }

    suspend fun editSchedule(scheduleId: Int, scheduleName: String, scheduleDescription: String, scheduleDaysPlannedOn: String) {
        scheduleDatabase.getDao().editSchedule(scheduleId, scheduleName, scheduleDescription, scheduleDaysPlannedOn)
    }

    suspend fun insertExercise(exercise: Exercise) {
        scheduleDatabase.getDao().insertExercise(exercise)
    }

    suspend fun insertScheduleExerciseCrossRef(crossRef: ScheduleExerciseCrossRef) {
        scheduleDatabase.getDao().insertScheduleExerciseCrossRef(crossRef)
    }

    suspend fun getExercisesOfSchedule(scheduleId: Int) = scheduleDatabase.getDao().getExercisesOfSchedule(scheduleId)

    suspend fun insertLog(log: Log) {
        scheduleDatabase.getDao().insertLog(log)
    }

    suspend fun getLogsOfExercise(exerciseId: Int) = scheduleDatabase.getDao().getLogsOfExercise(exerciseId)

    suspend fun findExerciseId(exerciseName: String) = scheduleDatabase.getDao().findExerciseId(exerciseName)
}