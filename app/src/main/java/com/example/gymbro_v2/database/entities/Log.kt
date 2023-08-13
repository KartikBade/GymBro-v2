package com.example.gymbro_v2.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Log(
    @PrimaryKey(autoGenerate = false)
    val time: String,
    val reps: Int,
    val weight: Int
)