package com.example.gymbro_v2.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Schedule

data class ExerciseWithSchedules(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseName",
        entityColumn = "scheduleName",
        associateBy = Junction(ScheduleExerciseCrossRef::class)
    )
    val schedule: List<Schedule>
)
