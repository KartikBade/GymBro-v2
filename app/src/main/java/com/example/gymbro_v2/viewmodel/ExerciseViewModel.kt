package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.relations.ExerciseWithLogs
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val todaysLogs: MutableLiveData<List<ExerciseWithLogs>> = MutableLiveData()
    val currentExercise: MutableLiveData<Exercise> = MutableLiveData()

    fun setCurrentExercise(exercise: Exercise) {
        currentExercise.value = exercise
    }

    fun getExerciseById() {
        viewModelScope.launch {
            currentExercise.value?.let {
                currentExercise.value = userRepository.getExerciseById(it.exerciseId)
            }
        }
    }

    fun insertLog(time: String, date: String, reps: Int, weight: Int) {
        viewModelScope.launch {
            currentExercise.value?.let {
                userRepository.insertLog(Log(0, it.exerciseId, time, date, reps, weight))
                todaysLogs.value = userRepository.getLogsOfExercise(it.exerciseId)
            }
        }
    }

    fun getLogsOfExercise() {
        viewModelScope.launch {
            currentExercise.value?.let {
                todaysLogs.value = userRepository.getLogsOfExercise(it.exerciseId)
            }
        }
    }

    fun editLog(weight: Int, reps: Int, logInt: Int) {
        viewModelScope.launch {
            currentExercise.value?.let {
                userRepository.editLog(weight, reps, logInt)
                todaysLogs.value = userRepository.getLogsOfExercise(it.exerciseId)
            }
        }
    }

    fun deleteLog(logId: Int) {
        viewModelScope.launch {
            currentExercise.value?.let {
                userRepository.deleteLog(logId)
                todaysLogs.value = userRepository.getLogsOfExercise(it.exerciseId)
            }
        }
    }

    fun editExercise(exerciseName: String, exerciseInstructions: String) {
        viewModelScope.launch {
            currentExercise.value?.let {
                userRepository.editExercise(it.exerciseId, exerciseName, exerciseInstructions)
                getExerciseById()
            }
        }
    }

    fun deleteExercise() {
        viewModelScope.launch {
            currentExercise.value?.let {
                userRepository.deleteExercise(it.exerciseId)
            }
        }
    }
}