package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.ActivityAddScheduleBinding
import com.example.gymbro_v2.model.Schedule
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.viewmodel.AddScheduleViewModel
import com.example.gymbro_v2.viewmodel.AddScheduleViewModelProviderFactory
import com.example.gymbro_v2.viewmodel.HomeViewModel
import com.example.gymbro_v2.viewmodel.HomeViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddScheduleActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var binding: ActivityAddScheduleBinding
    private lateinit var addScheduleViewModel: AddScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addScheduleViewModelProviderFactory = AddScheduleViewModelProviderFactory(userRepository)
        addScheduleViewModel = ViewModelProvider(this, addScheduleViewModelProviderFactory)[AddScheduleViewModel::class.java]

        binding.addScheduleButton.setOnClickListener {

            val scheduleName = binding.addScheduleEtName.text.toString()
            val scheduleDescription = binding.addScheduleEtDescription.text.toString()
            var scheduleDaysPlannedOn = ""
            if (binding.monday.isChecked) { scheduleDaysPlannedOn += "Mon" }
            if (binding.tuesday.isChecked) { scheduleDaysPlannedOn += "Tue" }
            if (binding.wednesday.isChecked) { scheduleDaysPlannedOn += "Wed" }
            if (binding.thursday.isChecked) { scheduleDaysPlannedOn += "Thu" }
            if (binding.friday.isChecked) { scheduleDaysPlannedOn += "Fri" }
            if (binding.saturday.isChecked) { scheduleDaysPlannedOn += "Sat" }
            if (binding.sunday.isChecked) { scheduleDaysPlannedOn += "Sun" }

            addScheduleViewModel.insertSchedule(Schedule(scheduleName, scheduleDescription, scheduleDaysPlannedOn))
            finish()
        }
    }
}