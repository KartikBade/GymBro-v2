package com.example.gymbro_v2.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.ExerciseAdapter
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.databinding.ActivityScheduleBinding
import com.example.gymbro_v2.viewmodel.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    lateinit var binding: ActivityScheduleBinding
    private lateinit var materialScheduleAlertDialog: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private val scheduleViewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materialScheduleAlertDialog = MaterialAlertDialogBuilder(binding.root.context)

        scheduleViewModel.scheduleId = intent.getIntExtra("scheduleId", -1)
        scheduleViewModel.scheduleName = intent.getStringExtra("scheduleName").toString().also {
            title = it
        }

        val exerciseAdapter = ExerciseAdapter {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("exerciseId", it.exerciseId)
            intent.putExtra("exerciseName", it.exerciseName)
            startActivity(intent)
        }

        binding.scheduleRvMain.adapter = exerciseAdapter
        scheduleViewModel.getExercisesOfSchedule()
        scheduleViewModel.exercisesOfSchedule.observe(this) {
            if (it.isNullOrEmpty() || it.first().exercises.isEmpty()) {
                binding.scheduleTvEmptyRv.visibility = View.VISIBLE
                return@observe
            }
            binding.scheduleTvEmptyRv.visibility = View.GONE
            exerciseAdapter.submitList(it.first().exercises)
        }

        binding.fabAddExercise.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_add_exercise, binding.root, false)
            launchCustomAlertDialog()
        }
    }

    private fun launchCustomAlertDialog() {
        val tvExerciseName: TextView = customAlertDialogView.findViewById(R.id.et_exercise_name)
        val tvExerciseInstructions: TextView = customAlertDialogView.findViewById(R.id.et_exercise_instructions)

        materialScheduleAlertDialog.setView(customAlertDialogView)
            .setTitle("Add Exercise")
            .setPositiveButton("Add") { dialog, _ ->
                val exerciseName = tvExerciseName.text.toString().trim()
                val exerciseInstructions = tvExerciseInstructions.text.toString().trim()

                if (exerciseName.isBlank()) {
                    Toast.makeText(this, "Failed: Add Name", Toast.LENGTH_LONG).show()
                } else if (exerciseInstructions.isBlank()) {
                    Toast.makeText(this, "Failed: Add Description", Toast.LENGTH_LONG).show()
                } else {
                    scheduleViewModel.insertExercise(Exercise(0, exerciseName, exerciseInstructions))
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}