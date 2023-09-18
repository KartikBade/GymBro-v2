package com.example.gymbro_v2.repository

import android.content.Context
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.ScheduleDatabase
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseFirestore,
    private val scheduleDatabase: ScheduleDatabase
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    suspend fun logout() {
        firebaseAuth.signOut()
        userSharedPref.edit().remove(context.getString(R.string.user_shared_pref_username)).apply()
        scheduleDatabase.getDao().deleteAllExercises()
        scheduleDatabase.getDao().deleteAllLogs()
        scheduleDatabase.getDao().deleteAllSchedules()
        scheduleDatabase.getDao().deleteScheduleExerciseCrossRefs()
    }

    fun getAllSchedules() = scheduleDatabase.getDao().getAllSchedules()

    suspend fun getScheduleById(scheduleId: Int) = scheduleDatabase.getDao().getScheduleById(scheduleId)

    suspend fun getExerciseById(exerciseId: Int) = scheduleDatabase.getDao().getExerciseById(exerciseId)

    suspend fun insertSchedule(schedule: Schedule) = scheduleDatabase.getDao().insertSchedule(schedule)

    suspend fun deleteSchedule(scheduleId: Int) = scheduleDatabase.getDao().deleteSchedule(scheduleId)

    suspend fun deleteExercise(exerciseId: Int) = scheduleDatabase.getDao().deleteExercise(exerciseId)

    suspend fun editSchedule(scheduleId: Int, scheduleName: String, scheduleDescription: String, scheduleDaysPlannedOn: String) = scheduleDatabase.getDao().editSchedule(scheduleId, scheduleName, scheduleDescription, scheduleDaysPlannedOn)

    suspend fun editExercise(exerciseId: Int, exerciseName: String, exerciseInstructions: String) = scheduleDatabase.getDao().editExercise(exerciseId, exerciseName, exerciseInstructions)

    suspend fun insertExercise(exercise: Exercise) = scheduleDatabase.getDao().insertExercise(exercise)

    suspend fun insertScheduleExerciseCrossRef(crossRef: ScheduleExerciseCrossRef) = scheduleDatabase.getDao().insertScheduleExerciseCrossRef(crossRef)

    suspend fun getExercisesOfSchedule(scheduleId: Int) = scheduleDatabase.getDao().getExercisesOfSchedule(scheduleId)

    suspend fun insertLog(log: Log) = scheduleDatabase.getDao().insertLog(log)

    suspend fun getLogsOfExercise(exerciseId: Int) = scheduleDatabase.getDao().getLogsOfExercise(exerciseId)

    suspend fun findExerciseId(exerciseName: String) = scheduleDatabase.getDao().findExerciseId(exerciseName)

    suspend fun editLog(weight: Int, reps: Int, logId: Int) = scheduleDatabase.getDao().editLog(weight, reps, logId)

    suspend fun deleteLog(logId: Int) = scheduleDatabase.getDao().deleteLog(logId)

    suspend fun dataBackup() {
        firebaseAuth.currentUser?.email?.let {
            firebaseDatabase.collection("users")
                .document(it)
        }
    }
}