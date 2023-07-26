package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.example.gymbro_v2.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddExerciseViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun insertExercise(exercise: Exercise, scheduleName: String) {
        viewModelScope.launch {
            userRepository.insertExercise(exercise)
            userRepository.insertScheduleExerciseCrossRef(ScheduleExerciseCrossRef(scheduleName, exercise.exerciseName))
        }
    }
}

class AddExerciseViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddExerciseViewModel(userRepository) as T
    }
}