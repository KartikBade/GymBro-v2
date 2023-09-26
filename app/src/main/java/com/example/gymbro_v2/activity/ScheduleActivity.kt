package com.example.gymbro_v2.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.ExerciseAdapter
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.database.entities.Schedule
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

        val id = intent.getIntExtra("scheduleId", -1)
        val name = intent.getStringExtra("scheduleName").toString()
        val description = intent.getStringExtra("scheduleDescription").toString()
        val daysPlannedOn = intent.getStringExtra("scheduleDaysPlannedOn").toString()
        scheduleViewModel.setCurrentSchedule(Schedule(id, name, description, daysPlannedOn))

        scheduleViewModel.currentSchedule.observe(this) { schedule ->
            schedule?.let {
                title = it.scheduleName
                binding.tvScheduleDescription.text = it.scheduleDescription
            }
        }

        val exerciseAdapter = ExerciseAdapter {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("exerciseId", it.exerciseId)
            intent.putExtra("exerciseName", it.exerciseName)
            intent.putExtra("exerciseInstructions", it.exerciseInstructions)
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

    override fun onResume() {
        scheduleViewModel.getScheduleById()
        scheduleViewModel.getExercisesOfSchedule()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.schedule_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_schedule -> {
                val intent = Intent(this, EditScheduleActivity::class.java)
                scheduleViewModel.currentSchedule.value?.let {
                    intent.putExtra("scheduleId", it.scheduleId)
                    intent.putExtra("oldScheduleName", it.scheduleName)
                    intent.putExtra("oldScheduleDescription", it.scheduleDescription)
                    intent.putExtra("oldScheduleDaysPlannedOn", it.scheduleDaysPlannedOn)
                }
                startActivity(intent)
                return true
            }
            R.id.delete_schedule -> {
                scheduleViewModel.deleteSchedule()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
            .setCancelable(false)
            .show()
    }
}