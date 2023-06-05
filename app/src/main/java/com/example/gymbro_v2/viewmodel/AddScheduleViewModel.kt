package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.model.Schedule
import com.example.gymbro_v2.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch {
            userRepository.insertSchedule(schedule)
        }
    }
}

class AddScheduleViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddScheduleViewModel(userRepository) as T
    }
}