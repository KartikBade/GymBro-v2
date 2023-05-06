package com.example.gymbro_v2.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.HomeActivity
import com.example.gymbro_v2.model.User
import com.example.gymbro_v2.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

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

class AuthViewModelProviderFactory(
    private val authRepository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository) as T
    }
}