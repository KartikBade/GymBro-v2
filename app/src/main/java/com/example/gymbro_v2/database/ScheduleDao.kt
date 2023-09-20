package com.example.gymbro_v2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ExerciseWithLogs
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.example.gymbro_v2.database.relations.ScheduleWithExercises


@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleExerciseCrossRef(crossRef: ScheduleExerciseCrossRef)

    @Query("SELECT * FROM scheduleexercisecrossref")
    fun getAllScheduleExerciseCrossRefs(): List<ScheduleExerciseCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: Log)

    @Query("SELECT * FROM schedule")
    fun getAllSchedules(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    suspend fun getScheduleById(scheduleId: Int): Schedule

    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): Exercise

    @Query("DELETE FROM schedule WHERE scheduleId = :scheduleId")
    suspend fun deleteSchedule(scheduleId: Int)

    @Query("DELETE FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun deleteExercise(exerciseId: Int)

    @Query("UPDATE schedule SET scheduleName = :scheduleName, scheduleDescription  = :scheduleDescription, scheduleDaysPlannedOn = :scheduleDaysPlannedOn WHERE scheduleId = :scheduleId")
    suspend fun editSchedule(scheduleId: Int, scheduleName: String, scheduleDescription: String, scheduleDaysPlannedOn: String)

    @Query("UPDATE exercise SET exerciseName = :exerciseName, exerciseInstructions  = :exerciseInstructions WHERE exerciseId = :exerciseId")
    suspend fun editExercise(exerciseId: Int, exerciseName: String, exerciseInstructions: String)

    @Query("DELETE FROM exercise")
    suspend fun deleteAllExercises()

    @Query("DELETE FROM log")
    suspend fun deleteAllLogs()

    @Query("DELETE FROM schedule")
    suspend fun deleteAllSchedules()

    @Query("DELETE FROM scheduleExerciseCrossRef")
    suspend fun deleteScheduleExerciseCrossRefs()

    @Transaction
    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    suspend fun getExercisesOfSchedule(scheduleId: Int): List<ScheduleWithExercises>

    @Transaction
    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getLogsOfExercise(exerciseId: Int): List<ExerciseWithLogs>

    @Query("UPDATE log SET weight = :weight, reps = :reps WHERE logId = :logId")
    suspend fun editLog(weight: Int, reps: Int, logId: Int)

    @Query("DELETE FROM log WHERE logId = :logId")
    suspend fun deleteLog(logId: Int)

    @Query("SELECT exerciseId FROM exercise WHERE exerciseName = :exerciseName LIMIT 1")
    suspend fun findExerciseId(exerciseName: String): Int
}