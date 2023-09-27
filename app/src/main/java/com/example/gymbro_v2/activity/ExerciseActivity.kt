package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.EditText
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

        val id = intent.getIntExtra("exerciseId", -1)
        val name = intent.getStringExtra("exerciseName").toString()
        val instructions = intent.getStringExtra("exerciseInstructions").toString()
        exerciseViewModel.setCurrentExercise(Exercise(id, name, instructions))

        exerciseViewModel.currentExercise.observe(this) { exercise ->
            exercise?.let {
                binding.tvExerciseTitle.text = it.exerciseName
            }
        }

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
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }

        exerciseViewModel.getLogsOfExercise()
        val logAdapter = LogAdapter {
            launchCustomAlertDialog(it)
        }
        binding.rvTodaysLogs.adapter = logAdapter
        exerciseViewModel.todaysLogs.observe(this) {
            logAdapter.submitList(it.first().logs)
            if (it.first().logs.isEmpty()) {
                binding.linearLayoutLogItemLabels.visibility = View.GONE
                binding.tvEmptyLogs.visibility = View.VISIBLE
            } else {
                binding.linearLayoutLogItemLabels.visibility = View.VISIBLE
                binding.tvEmptyLogs.visibility = View.GONE
            }
        }

        binding.ivDecReps.setOnClickListener {
            try {
                val reps = binding.etReps.text.toString().toInt() - 1
                if (reps < 0) {
                    binding.etReps.setText("0")
                    return@setOnClickListener
                }
                binding.etReps.setText(reps.toString())
            } catch (_: Exception) {}
        }
        binding.ivIncReps.setOnClickListener {
            try {
                if (binding.etReps.text.isBlank()) {
                    binding.etReps.setText("1")
                    return@setOnClickListener
                }
                val reps = binding.etReps.text.toString().toInt() + 1
                if (reps < 0) { return@setOnClickListener }
                binding.etReps.setText(reps.toString())
            } catch (_: Exception) {}
        }
        binding.ivDecWeight.setOnClickListener {
            try {
                val weight = binding.etWeight.text.toString().toInt() - 5
                if (weight < 0) {
                    binding.etWeight.setText("0")
                    return@setOnClickListener
                }
                binding.etWeight.setText(weight.toString())
            } catch (_: Exception) {}
        }
        binding.ivIncWeight.setOnClickListener {
            try {
                if (binding.etWeight.text.isBlank()) {
                    binding.etWeight.setText("5")
                    return@setOnClickListener
                }
                val weight = binding.etWeight.text.toString().toInt() + 5
                if (weight < 0) { return@setOnClickListener }
                binding.etWeight.setText(weight.toString())
            } catch (_: Exception) {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.exercise_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_exercise -> {
                launchCustomAlertDialog()
                return true
            }
            R.id.delete_exercise -> {
                exerciseViewModel.deleteExercise()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
            .setCancelable(false)
            .show()
    }

    private fun launchCustomAlertDialog() {
        val customAlertDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_edit_exercise, binding.root, false)
        val etEditName: EditText = customAlertDialogView.findViewById(R.id.et_edit_name)
        val etEditInstructions: EditText = customAlertDialogView.findViewById(R.id.et_edit_instructions)

        exerciseViewModel.currentExercise.value?.let {
            etEditName.setText(it.exerciseName)
            etEditInstructions.setText(it.exerciseInstructions)
        }

        materialScheduleAlertDialog.setView(customAlertDialogView)
            .setTitle("Edit Exercise")
            .setPositiveButton("Edit") { dialog, _ ->
                val exerciseName = etEditName.text.toString().trim()
                val exerciseInstructions = etEditInstructions.text.toString().trim()

                if (exerciseName.isBlank() || exerciseInstructions.isBlank()) {
                    Toast.makeText(this, "Invalid Input.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                exerciseViewModel.editExercise(exerciseName, exerciseInstructions)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}