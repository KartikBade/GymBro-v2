package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.relations.ExerciseWithSchedules
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.example.gymbro_v2.database.relations.ScheduleWithExercises
import com.example.gymbro_v2.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var exercisesOfSchedule: MutableLiveData<List<ScheduleWithExercises>> = MutableLiveData()

    fun getExercisesOfSchedule(scheduleName: String) {
        viewModelScope.launch {
            exercisesOfSchedule.value = userRepository.getExercisesOfSchedule(scheduleName)
        }
    }

    fun insertExercise(exercise: Exercise, scheduleName: String) {
        viewModelScope.launch {
            userRepository.insertExercise(exercise)
            userRepository.insertScheduleExerciseCrossRef(ScheduleExerciseCrossRef(scheduleName, exercise.exerciseName))
            getExercisesOfSchedule(scheduleName)
        }
    }
}

class ScheduleViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleViewModel(userRepository) as T
    }
}