package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    var exerciseName = ""
    var exerciseId = -1

    fun insertLog(time: String, date: String, reps: Int, weight: Int) {
        viewModelScope.launch {
            userRepository.insertLog(Log(0, exerciseId, time, date, reps, weight))
            todaysLogs.value = userRepository.getLogsOfExercise(exerciseId)
        }
    }

    fun getLogsOfExercise() {
        viewModelScope.launch {
            todaysLogs.value = userRepository.getLogsOfExercise(exerciseId)
        }
    }
}