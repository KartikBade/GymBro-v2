package com.example.gymbro_v2.viewmodel

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun getUserEmail(): String = userRepository.getUserEmail().toString()

    fun getProfilePhotoLetter() = userRepository.getProfilePhotoLetter().toString()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getAllSchedules() = userRepository.getAllSchedules()
}