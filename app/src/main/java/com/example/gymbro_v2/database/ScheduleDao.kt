package com.example.gymbro_v2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymbro_v2.model.Schedule

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedules")
    fun getAllSchedules(): LiveData<List<Schedule>>

    @Query("DELETE FROM schedules WHERE name = :scheduleName")
    suspend fun deleteSchedule(scheduleName: String)

    @Query("UPDATE schedules SET name = :newName, description  = :description, daysPlannedOn = :daysPlannedOn WHERE name = :oldName")
    suspend fun editSchedule(oldName: String, newName: String, description: String, daysPlannedOn: String)
}