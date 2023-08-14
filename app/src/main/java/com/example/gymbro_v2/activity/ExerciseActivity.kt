package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.LogAdapter
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.databinding.ActivityExerciseBinding
import com.example.gymbro_v2.viewmodel.ExerciseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExerciseActivity : AppCompatActivity() {

    lateinit var binding: ActivityExerciseBinding
    private val exerciseViewModel: ExerciseViewModel by viewModels()
    private lateinit var materialScheduleAlertDialog: MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materialScheduleAlertDialog = MaterialAlertDialogBuilder(binding.root.context)

        val exerciseId = intent.getIntExtra("exerciseId", -1)
        val exerciseName = intent.getStringExtra("exerciseName").toString()
        binding.tvExerciseTitle.text = exerciseName
        exerciseViewModel.exerciseName = exerciseName
        exerciseViewModel.exerciseId = exerciseId

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

                if (weight < 0 || reps < 0) {
                    Toast.makeText(this, "Please enter positive values", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                exerciseViewModel.insertLog(time, date, reps, weight)
            } catch (e: ClassCastException) {
                Toast.makeText(this, "Invalid values entered", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Something when wrong!", Toast.LENGTH_SHORT).show()
            }
        }

        val logAdapter = LogAdapter {
            launchCustomAlertDialog(it)
        }
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

    private fun launchCustomAlertDialog(log: Log) {
        val customAlertDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_edit_log, binding.root, false)
        val etEditWeight: TextView = customAlertDialogView.findViewById(R.id.et_edit_weight)
        val etEditReps: TextView = customAlertDialogView.findViewById(R.id.et_edit_reps)
        etEditWeight.text = log.weight.toString()
        etEditReps.text = log.reps.toString()

        materialScheduleAlertDialog.setView(customAlertDialogView)
            .setTitle("Edit Log")
            .setPositiveButton("Edit") { dialog, _ ->
                val weightString = etEditWeight.text.toString().trim()
                val repsString = etEditReps.text.toString().trim()

                try {
                    val weight = weightString.toInt()
                    val reps = repsString.toInt()
                    if (weight < 0 || reps < 0) {
                        Toast.makeText(this, "Please enter positive values", Toast.LENGTH_SHORT).show()
                    }
                    exerciseViewModel.editLog(weight, reps, log.logId)
                    dialog.dismiss()
                } catch (e: ClassCastException) {
                    Toast.makeText(this, "Please enter numbers.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            .setNeutralButton("Delete") { dialog, _ ->
                exerciseViewModel.deleteLog(log.logId)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}