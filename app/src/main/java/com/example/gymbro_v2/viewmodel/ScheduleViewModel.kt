package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Schedule
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
    val currentSchedule: MutableLiveData<Schedule> = MutableLiveData()

    fun setCurrentSchedule(schedule: Schedule) {
        currentSchedule.value = schedule
    }

    fun getScheduleById() {
        viewModelScope.launch {
            currentSchedule.value?.let {
                currentSchedule.value = userRepository.getScheduleById(it.scheduleId)
            }
        }
    }

    fun getExercisesOfSchedule() {
        viewModelScope.launch {
            currentSchedule.value?.let {
                exercisesOfSchedule.value = userRepository.getExercisesOfSchedule(it.scheduleId)
            }
        }
    }

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            currentSchedule.value?.let {
                userRepository.insertExercise(exercise)
                val exerciseId = userRepository.findExerciseId(exercise.exerciseName)
                userRepository.insertScheduleExerciseCrossRef(ScheduleExerciseCrossRef(it.scheduleId, exerciseId))
                getExercisesOfSchedule()
            }
        }
    }

    fun deleteSchedule() {
        viewModelScope.launch {
            currentSchedule.value?.let {
                userRepository.deleteSchedule(it.scheduleId)
            }
        }
    }
}