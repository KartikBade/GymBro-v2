package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.model.Schedule
import com.example.gymbro_v2.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun editSchedule(oldName: String, newName: String, description: String, daysPlannedOn: String) {
        viewModelScope.launch {
            userRepository.editSchedule(oldName, newName, description, daysPlannedOn)
        }
    }

    fun deleteSchedule(scheduleName: String) {
        viewModelScope.launch {
            userRepository.deleteSchedule(scheduleName)
        }
    }
}

class EditScheduleViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditScheduleViewModel(userRepository) as T
    }
}