package com.example.gymbro_v2.viewmodel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.model.Exercise
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun logout() {
        userRepository.logout()
    }

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            userRepository.insertExercise(exercise)
        }
    }

    fun getAllExercise() = userRepository.getAllExercise()

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            userRepository.deleteExercise(exercise)
        }
    }
}

class UserViewModelProviderFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}