package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun editSchedule(scheduleId: Int, scheduleName: String, scheduleDescription: String, scheduleDaysPlannedOn: String) {
        viewModelScope.launch {
            userRepository.editSchedule(scheduleId, scheduleName, scheduleDescription, scheduleDaysPlannedOn)
        }
    }

    fun deleteSchedule(scheduleId: Int) {
        viewModelScope.launch {
            userRepository.deleteSchedule(scheduleId)
        }
    }
}