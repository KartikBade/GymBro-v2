package com.example.gymbro_v2.database.relations

import androidx.room.Entity

@Entity(primaryKeys = ["scheduleName", "exerciseName"])
data class ScheduleExerciseCrossRef(
    val scheduleName: String,
    val exerciseName: String
)