package com.example.gymbro_v2.repository

import android.content.Context
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.ScheduleDatabase
import com.example.gymbro_v2.model.Schedule
import com.google.firebase.auth.FirebaseAuth

class UserRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val scheduleDatabase: ScheduleDatabase
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    fun logout() {
        firebaseAuth.signOut()
        userSharedPref.edit().remove(context.getString(R.string.user_shared_pref_username)).apply()
    }

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleDatabase.getDao().insertSchedule(schedule)
    }

    fun getAllSchedule() = scheduleDatabase.getDao().getAllSchedules()

    suspend fun deleteSchedule(schedule: Schedule) {
        scheduleDatabase.getDao().deleteSchedule(schedule)
    }
}