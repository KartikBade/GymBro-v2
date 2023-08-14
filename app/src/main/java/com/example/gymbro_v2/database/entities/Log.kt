package com.example.gymbro_v2.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Log(
    @PrimaryKey(autoGenerate = true)
    val logId: Int,
    val exerciseId: Int,
    val time: String,
    val date: String,
    val reps: Int,
    val weight: Int
)