package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.databinding.ActivityAddScheduleBinding
import com.example.gymbro_v2.viewmodel.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddScheduleBinding
    private val addScheduleViewModel: AddScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            addScheduleViewModel.insertSchedule(Schedule(0, scheduleName, scheduleDescription, scheduleDaysPlannedOn))
            finish()
        }
    }
}