package com.example.gymbro_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.model.User
import com.example.gymbro_v2.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun signupUser(user: User, password: String) {
        viewModelScope.launch {
            authRepository.signupUser(user, password)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginUser(email, password)
        }
    }
}