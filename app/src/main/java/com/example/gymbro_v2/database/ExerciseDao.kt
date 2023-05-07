package com.example.gymbro_v2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymbro_v2.model.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}