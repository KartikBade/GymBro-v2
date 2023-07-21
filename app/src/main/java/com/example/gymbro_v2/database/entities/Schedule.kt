package com.example.gymbro_v2.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val description: String,
    val daysPlannedOn: String,
    val totalExercises: Int = 0
)
