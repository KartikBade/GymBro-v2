package com.example.gymbro_v2.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.example.gymbro_v2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(
    private val context: Application,
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseFirestore
) {

    suspend fun createNewUser(email: String, password: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerNewUser(User(email, password, phone))
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
                    Toast.makeText(context, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerNewUser(user: User) {

        val newUser = hashMapOf(
            "email" to user.email,
            "username" to user.username,
            "phone" to user.phone
        )

        firebaseDatabase.collection("users")
            .document(user.email)
            .set(newUser)
            .addOnFailureListener {
                Log.e("UserRepository", "Error Adding New User: ${it.message}")
            }
    }
}