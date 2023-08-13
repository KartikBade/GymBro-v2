package com.example.gymbro_v2.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log

//data class ExerciseWithLogs(
//    @Embedded val exercise: Exercise,
//    @Relation(
//        parentColumn = "exerciseName",
//        entityColumn = "time"
//    )
//    val logs: List<Log>
//)