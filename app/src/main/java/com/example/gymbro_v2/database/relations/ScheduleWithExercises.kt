package com.example.gymbro_v2.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Schedule

data class ScheduleWithExercises(
        @Embedded val schedule: Schedule,
        @Relation(
                parentColumn = "scheduleId",
                entityColumn = "exerciseId",
                associateBy = Junction(ScheduleExerciseCrossRef::class)
        )
        val exercises: List<Exercise>
)