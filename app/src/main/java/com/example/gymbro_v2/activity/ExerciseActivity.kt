package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gymbro_v2.adapter.LogAdapter
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.databinding.ActivityExerciseBinding
import com.example.gymbro_v2.viewmodel.ExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExerciseActivity : AppCompatActivity() {

    lateinit var binding: ActivityExerciseBinding
    lateinit var exerciseName: String
    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exerciseName = intent.getStringExtra("exerciseName").toString()
        binding.tvExerciseTitle.text = exerciseName
        exerciseViewModel.exerciseName = exerciseName

        val sdfDate = SimpleDateFormat("dd-MM-yyyy")
        val sdfTime = SimpleDateFormat("HH:mm:ss")
        val d = Date()
        val date: String = sdfDate.format(d)
        val time: String = sdfTime.format(d)

        binding.btnCompleteSet.setOnClickListener {
            val weightString = binding.etWeight.text.toString().trim()
            val repsString = binding.etReps.text.toString().trim()

            try {
                val weight = weightString.toInt()
                val reps = repsString.toInt()

                exerciseViewModel.insertLog(time, date, reps, weight)
            } catch (e: ClassCastException) {
                Toast.makeText(this, "Invalid values entered", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Something when wrong!", Toast.LENGTH_SHORT).show()
            }
        }

        val logAdapter = LogAdapter()
        binding.rvTodaysLogs.adapter = logAdapter
        exerciseViewModel.todaysLogs.observe(this) {
            val finalList = mutableListOf<Log>()
            for (i in it.first().logs) {
                if (i.date == date) {
                    finalList.add(i)
                }
            }
            logAdapter.submitList(finalList)
        }
        exerciseViewModel.getLogsOfExercise()
    }
}