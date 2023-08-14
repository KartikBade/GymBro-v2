package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gymbro_v2.databinding.ActivityEditScheduleBinding
import com.example.gymbro_v2.viewmodel.EditScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditScheduleActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditScheduleBinding
    private val editScheduleViewModel: EditScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleId = intent.getIntExtra("scheduleId", -1)
        val oldScheduleName = intent.getStringExtra("oldScheduleName").toString()
        val scheduleDescription = intent.getStringExtra("oldScheduleDescription").toString()
        val scheduleDaysPlannedOn = intent.getStringExtra("oldScheduleDaysPlannedOn").toString()

        binding.editScheduleEtName.setText(oldScheduleName)
        binding.editScheduleEtDescription.setText(scheduleDescription)
        if (scheduleDaysPlannedOn.contains("Mon")) { binding.monday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Tue")) { binding.tuesday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Wed")) { binding.wednesday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Thu")) { binding.thursday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Fri")) { binding.friday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Sat")) { binding.saturday.isChecked = true }
        if (scheduleDaysPlannedOn.contains("Sun")) { binding.sunday.isChecked = true }

        binding.saveScheduleButton.setOnClickListener {
            val newScheduleName = binding.editScheduleEtName.text.toString()
            val newScheduleDescription = binding.editScheduleEtDescription.text.toString()
            var newScheduleDaysPlannedOn = ""
            if (binding.monday.isChecked) { newScheduleDaysPlannedOn += "Mon" }
            if (binding.tuesday.isChecked) { newScheduleDaysPlannedOn += "Tue" }
            if (binding.wednesday.isChecked) { newScheduleDaysPlannedOn += "Wed" }
            if (binding.thursday.isChecked) { newScheduleDaysPlannedOn += "Thu" }
            if (binding.friday.isChecked) { newScheduleDaysPlannedOn += "Fri" }
            if (binding.saturday.isChecked) { newScheduleDaysPlannedOn += "Sat" }
            if (binding.sunday.isChecked) { newScheduleDaysPlannedOn += "Sun" }

            editScheduleViewModel.editSchedule(scheduleId, newScheduleName, newScheduleDescription, newScheduleDaysPlannedOn)
            finish()
        }

        binding.deleteScheduleButton.setOnClickListener {
            editScheduleViewModel.deleteSchedule(scheduleId)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}