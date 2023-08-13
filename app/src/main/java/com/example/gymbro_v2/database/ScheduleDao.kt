package com.example.gymbro_v2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.example.gymbro_v2.database.relations.ScheduleWithExercises


@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSchedule(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertScheduleExerciseCrossRef(crossRef: ScheduleExerciseCrossRef)

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    suspend fun insertLog(log: Log)

    @Query("SELECT * FROM schedule")
    fun getAllSchedules(): LiveData<List<Schedule>>

    @Query("DELETE FROM schedule WHERE scheduleName = :scheduleName")
    suspend fun deleteSchedule(scheduleName: String)

    @Query("UPDATE schedule SET scheduleName = :newName, scheduleDescription  = :description, scheduleDaysPlannedOn = :daysPlannedOn WHERE scheduleName = :oldName")
    suspend fun editSchedule(oldName: String, newName: String, description: String, daysPlannedOn: String)

    @Query("DELETE FROM schedule")
    suspend fun deleteAllSchedules()

    @Transaction
    @Query("SELECT * FROM schedule WHERE scheduleId = (SELECT scheduleId FROM schedule WHERE scheduleName = :scheduleName)")
    suspend fun getExercisesOfSchedule(scheduleName: String): List<ScheduleWithExercises>

    @Query("SELECT scheduleId FROM schedule WHERE scheduleName = :scheduleName LIMIT 1")
    suspend fun findScheduleId(scheduleName: String): Int

    @Query("SELECT exerciseId FROM exercise WHERE exerciseName = :exerciseName LIMIT 1")
    suspend fun findExerciseId(exerciseName: String): Int

//    @Transaction
//    @Query("SELECT * FROM log WHERE exerciseName = :exerciseName")
//    suspend fun getLogsOfExercise(exerciseName: String): List<ExerciseWithLogs>
}