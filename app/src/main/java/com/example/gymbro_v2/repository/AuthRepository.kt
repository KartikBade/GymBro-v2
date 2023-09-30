package com.example.gymbro_v2.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.HomeActivity
import com.example.gymbro_v2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseFirestore
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    suspend fun signupUser(user: User, password: String) {
        try {
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addToFirestore(user)

                        userSharedPref.edit()
                            .putString(
                                context.getString(R.string.user_shared_pref_username),
                                user.username
                            )
                            .apply()

                    } else {
                        Log.e("AuthRepo", task.exception.toString())
                    }
                }.await()
        } catch (e: java.lang.Exception) {
            Log.e("AuthRepo", "Error: ${e.localizedMessage}")
        }
    }

    suspend fun loginUser(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        CoroutineScope(Dispatchers.IO).launch {
                            firebaseDatabase
                                .collection("users")
                                .document(email)
                                .get()
                                .addOnSuccessListener { doc ->
                                    doc.getString(context.getString(R.string.user_firebase_username))
                                        ?.let { username ->

                                            userSharedPref.edit()
                                                .putString(
                                                    context.getString(R.string.user_shared_pref_username),
                                                    username
                                                )
                                                .apply()

                                            val intent = Intent(context, HomeActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            context.startActivity(intent)
                                        }
                                }.await()
                        }
                    } else {
                        Toast.makeText(context, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                    }
                }.await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Unknown Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToFirestore(user: User) {

        val newUser = hashMapOf(
            context.getString(R.string.user_firebase_email) to user.email,
            context.getString(R.string.user_firebase_username) to user.username,
            context.getString(R.string.user_firebase_phone) to user.phone
        )

        firebaseDatabase.collection("users")
            .document(user.email)
            .set(newUser)
            .addOnFailureListener {
                Log.e("UserRepository", "Error Adding New User: ${it.message}")
            }
    }
}