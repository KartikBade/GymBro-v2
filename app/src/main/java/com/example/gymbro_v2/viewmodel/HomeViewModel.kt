package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.model.Schedule
import com.example.gymbro_v2.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun logout() {
        userRepository.logout()
    }

    fun getAllSchedule() = userRepository.getAllSchedule()

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch {
            userRepository.deleteSchedule(schedule)
        }
    }
}

class HomeViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository) as T
    }
}