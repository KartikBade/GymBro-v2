package com.example.gymbro_v2.repository

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val context: Application,
    private val auth: FirebaseAuth
) {

    suspend fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("AuthRepo", "User ${auth.currentUser?.displayName} Created")
                } else {
                    Log.e("AuthRepo", task.exception.toString())
                }
            }
    }

    suspend fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("AuthRepo", "User ${auth.currentUser?.displayName} Logged In")
                } else {
                    Log.e("AuthRepo", task.exception.toString())
                }
            }
    }
}