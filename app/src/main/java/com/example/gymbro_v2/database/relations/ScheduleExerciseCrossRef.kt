package com.example.gymbro_v2.database.relations

import androidx.room.Entity

@Entity(primaryKeys = ["scheduleId", "exerciseId"])
data class ScheduleExerciseCrossRef(
    val scheduleId: Int,
    val exerciseId: Int
)