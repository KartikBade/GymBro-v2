package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.example.gymbro_v2.database.relations.ScheduleWithExercises
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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
            val scheduleId = userRepository.findScheduleId(scheduleName)
            val exerciseId = userRepository.findExerciseId(exercise.exerciseName)
            userRepository.insertScheduleExerciseCrossRef(ScheduleExerciseCrossRef(scheduleId, exerciseId))
            getExercisesOfSchedule(scheduleName)
        }
    }
}