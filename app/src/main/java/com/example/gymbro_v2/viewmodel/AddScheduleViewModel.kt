package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch {
            userRepository.insertSchedule(schedule)
        }
    }
}