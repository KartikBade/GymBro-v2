package com.example.gymbro_v2.repository

import android.content.Context
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.ScheduleDatabase
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.database.relations.ScheduleExerciseCrossRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseFirestore,
    private val scheduleDatabase: ScheduleDatabase
) {

    private val userSharedPref = context.getSharedPreferences(context.getString(R.string.user_shared_pref), 0)

    suspend fun logout() {
        firebaseAuth.signOut()
        userSharedPref.edit().remove(context.getString(R.string.user_shared_pref_username)).apply()
        scheduleDatabase.getDao().deleteAllExercises()
        scheduleDatabase.getDao().deleteAllLogs()
        scheduleDatabase.getDao().deleteAllSchedules()
        scheduleDatabase.getDao().deleteScheduleExerciseCrossRefs()
    }

    fun getAllSchedules() = scheduleDatabase.getDao().getAllSchedules()

    suspend fun getScheduleById(scheduleId: Int) = scheduleDatabase.getDao().getScheduleById(scheduleId)

    suspend fun getExerciseById(exerciseId: Int) = scheduleDatabase.getDao().getExerciseById(exerciseId)

    suspend fun insertSchedule(schedule: Schedule) = scheduleDatabase.getDao().insertSchedule(schedule)

    suspend fun deleteSchedule(scheduleId: Int) = scheduleDatabase.getDao().deleteSchedule(scheduleId)

    suspend fun deleteExercise(exerciseId: Int) = scheduleDatabase.getDao().deleteExercise(exerciseId)

    suspend fun editSchedule(scheduleId: Int, scheduleName: String, scheduleDescription: String, scheduleDaysPlannedOn: String) = scheduleDatabase.getDao().editSchedule(scheduleId, scheduleName, scheduleDescription, scheduleDaysPlannedOn)

    suspend fun editExercise(exerciseId: Int, exerciseName: String, exerciseInstructions: String) = scheduleDatabase.getDao().editExercise(exerciseId, exerciseName, exerciseInstructions)

    suspend fun insertExercise(exercise: Exercise) = scheduleDatabase.getDao().insertExercise(exercise)

    suspend fun insertScheduleExerciseCrossRef(crossRef: ScheduleExerciseCrossRef) = scheduleDatabase.getDao().insertScheduleExerciseCrossRef(crossRef)

    suspend fun getExercisesOfSchedule(scheduleId: Int) = scheduleDatabase.getDao().getExercisesOfSchedule(scheduleId)

    suspend fun insertLog(log: Log) = scheduleDatabase.getDao().insertLog(log)

    suspend fun getLogsOfExercise(exerciseId: Int) = scheduleDatabase.getDao().getLogsOfExercise(exerciseId)

    suspend fun findExerciseId(exerciseName: String) = scheduleDatabase.getDao().findExerciseId(exerciseName)

    suspend fun editLog(weight: Int, reps: Int, logId: Int) = scheduleDatabase.getDao().editLog(weight, reps, logId)

    suspend fun deleteLog(logId: Int) = scheduleDatabase.getDao().deleteLog(logId)

    suspend fun dataBackup(): Boolean {
        var scheduleSuccess = true
        var exerciseSuccess = true
        var logSuccess = true

        firebaseAuth.currentUser?.email?.let { email ->
            val listOfSchedules = scheduleDatabase.getDao().getAllSchedulesAsList()
            for (schedule in listOfSchedules) {
                val scheduleData = hashMapOf(
                    "ScheduleId" to schedule.scheduleId,
                    "ScheduleName" to schedule.scheduleName,
                    "ScheduleDescription" to schedule.scheduleDescription,
                    "ScheduleDaysPlannedOn" to schedule.scheduleDaysPlannedOn
                )

                firebaseDatabase.collection("users")
                    .document(email)
                    .collection("schedules")
                    .document(schedule.scheduleId.toString())
                    .set(scheduleData)
                    .addOnFailureListener {
                        scheduleSuccess = false
                    }

                val listOfScheduleWithExercises =
                    getExercisesOfSchedule(schedule.scheduleId).first()
                val listOfExercises = listOfScheduleWithExercises.exercises
                for (exercise in listOfExercises) {
                    val exerciseData = hashMapOf(
                        "ExerciseId" to exercise.exerciseId,
                        "ExerciseName" to exercise.exerciseName,
                        "ExerciseInstructions" to exercise.exerciseInstructions
                    )

                    firebaseDatabase.collection("users")
                        .document(email)
                        .collection("schedules")
                        .document(schedule.scheduleId.toString())
                        .collection("exercises")
                        .document(exercise.exerciseId.toString())
                        .set(exerciseData)
                        .addOnFailureListener {
                            exerciseSuccess = false
                        }

                    val listOfExerciseWithLogs = getLogsOfExercise(exercise.exerciseId).first()
                    val listOfLogs = listOfExerciseWithLogs.logs
                    for (log in listOfLogs) {
                        val logData = hashMapOf(
                            "LogId" to log.logId,
                            "ExerciseId" to log.exerciseId,
                            "Time" to log.time,
                            "Date" to log.date,
                            "Reps" to log.reps,
                            "Weight" to log.weight
                        )

                        firebaseDatabase.collection("users")
                            .document(email)
                            .collection("schedules")
                            .document(schedule.scheduleId.toString())
                            .collection("exercises")
                            .document(exercise.exerciseId.toString())
                            .collection("logs")
                            .document(log.logId.toString())
                            .set(logData)
                            .addOnFailureListener {
                                logSuccess = false
                            }
                    }
                }
            }
        }
        if (scheduleSuccess && exerciseSuccess && logSuccess) {
            return true
        }
        return false
    }

    suspend fun dataImport() {
        val scheduleList: MutableList<Schedule> = mutableListOf()
        val exerciseList: MutableList<Exercise> = mutableListOf()
        val logList: MutableList<Log> = mutableListOf()
        val crossRefList: MutableList<ScheduleExerciseCrossRef> = mutableListOf()

        firebaseAuth.currentUser?.email?.let { email ->
            firebaseDatabase.collection("users")
                .document(email)
                .collection("schedules")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.apply {
                             scheduleList.add(Schedule(
                                data["ScheduleId"].toString().toInt(),
                                data["ScheduleName"].toString(),
                                data["ScheduleDescription"].toString(),
                                data["ScheduleDaysPlannedOn"].toString())
                            )
                        }
                    }
                }.await()

            for (schedule in scheduleList) {
                firebaseDatabase.collection("users")
                    .document(email)
                    .collection("schedules")
                    .document(schedule.scheduleId.toString())
                    .collection("exercises")
                    .get()
                    .addOnSuccessListener { resultExercise ->
                        for (documentExercise in resultExercise) {
                            documentExercise.apply {
                                exerciseList.add(Exercise(
                                    data["ExerciseId"].toString().toInt(),
                                    data["ExerciseName"].toString(),
                                    data["ExerciseInstructions"].toString())
                                )
                                crossRefList.add(ScheduleExerciseCrossRef(
                                    schedule.scheduleId,
                                    documentExercise.data["ExerciseId"].toString().toInt())
                                )
                            }
                        }
                    }.await()

                for (exercise in exerciseList) {
                    firebaseDatabase.collection("users")
                        .document(email)
                        .collection("schedules")
                        .document(schedule.scheduleId.toString())
                        .collection("exercises")
                        .document(exercise.exerciseId.toString())
                        .collection("logs")
                        .get()
                        .addOnSuccessListener { resultLog ->
                            for (documentLog in resultLog) {
                                documentLog.apply {
                                    logList.add( Log(
                                        data["LogId"].toString().toInt(),
                                        data["ExerciseId"].toString().toInt(),
                                        data["Time"].toString(),
                                        data["Date"].toString(),
                                        data["Reps"].toString().toInt(),
                                        data["Weight"].toString().toInt())
                                    )
                                }
                            }
                        }.await()
                }
            }
        }
        scheduleDatabase.getDao().deleteAllExercises()
        scheduleDatabase.getDao().deleteAllLogs()
        scheduleDatabase.getDao().deleteAllSchedules()
        scheduleDatabase.getDao().deleteScheduleExerciseCrossRefs()
        for (i in scheduleList) { insertSchedule(i) }
        for (i in exerciseList) { insertExercise(i) }
        for (i in logList) { insertLog(i) }
        for (i in crossRefList) { insertScheduleExerciseCrossRef(i) }
    }
}