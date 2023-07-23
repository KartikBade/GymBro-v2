package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.adapter.ExerciseAdapter
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.databinding.ActivityScheduleBinding
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.viewmodel.ScheduleViewModel
import com.example.gymbro_v2.viewmodel.ScheduleViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var binding: ActivityScheduleBinding
    lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleName = intent.getStringExtra("scheduleName").toString()
        title = scheduleName

        val scheduleViewModelProviderFactory = ScheduleViewModelProviderFactory(userRepository)
        scheduleViewModel = ViewModelProvider(this, scheduleViewModelProviderFactory)[ScheduleViewModel::class.java]

        val exerciseAdapter = ExerciseAdapter()

        binding.scheduleRvMain.adapter = exerciseAdapter
        scheduleViewModel.getExercisesOfSchedule(scheduleName)
        scheduleViewModel.exercisesOfSchedule.observe(this) {
            Log.e("ScheduleActivity", it.first().toString())
            if (it.isNullOrEmpty() || it.first().exercises.isEmpty()) {
                binding.scheduleTvEmptyRv.visibility = View.VISIBLE
                return@observe
            }
            else {
                binding.scheduleTvEmptyRv.visibility = View.GONE
            }
            exerciseAdapter.submitList(it.first().exercises)
        }

        binding.fabAddExercise.setOnClickListener {
            scheduleViewModel.insertExercise(Exercise("Dips", "2 Sets of 10 Reps."), scheduleName)
        }
    }
}