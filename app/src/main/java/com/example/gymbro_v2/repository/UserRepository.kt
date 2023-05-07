package com.example.gymbro_v2.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.AuthActivity
import com.example.gymbro_v2.activity.HomeActivity
import com.example.gymbro_v2.database.ExerciseDatabase
import com.example.gymbro_v2.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ActivityScoped

class UserRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val exerciseDatabase: ExerciseDatabase
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    fun logout() {
        firebaseAuth.signOut()
        userSharedPref.edit().remove(context.getString(R.string.user_shared_pref_username)).apply()
    }

    suspend fun insertExercise(exercise: Exercise) {
        exerciseDatabase.getDao().insertExercise(exercise)
    }

    fun getAllExercise() = exerciseDatabase.getDao().getAllExercises()
}