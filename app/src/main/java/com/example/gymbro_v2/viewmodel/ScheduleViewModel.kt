package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.repository.UserRepository
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

}

class ScheduleViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleViewModel(userRepository) as T
    }
}