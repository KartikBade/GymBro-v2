package com.example.gymbro_v2.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Int,
    val scheduleName: String,
    val scheduleDescription: String,
    val scheduleDaysPlannedOn: String
)