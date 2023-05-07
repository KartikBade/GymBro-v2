package com.example.gymbro_v2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("exercises")
data class Exercise(
    @PrimaryKey
    val name: String,
    val description: String,
    var status: Boolean = false
)
