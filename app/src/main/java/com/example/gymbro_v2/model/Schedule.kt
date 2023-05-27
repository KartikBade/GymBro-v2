package com.example.gymbro_v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("schedules")
data class Schedule(
    @PrimaryKey
    val name: String,
    val description: String,
    val daysPlannedOn: String,
    val totalExercises: Int = 0
)
