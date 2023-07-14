package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.ActivityEditScheduleBinding
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.viewmodel.AddScheduleViewModel
import com.example.gymbro_v2.viewmodel.AddScheduleViewModelProviderFactory
import com.example.gymbro_v2.viewmodel.EditScheduleViewModel
import com.example.gymbro_v2.viewmodel.EditScheduleViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class EditScheduleActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var binding: ActivityEditScheduleBinding
    private lateinit var editScheduleViewModel: EditScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editScheduleViewModelProviderFactory = EditScheduleViewModelProviderFactory(userRepository)
        editScheduleViewModel = ViewModelProvider(this, editScheduleViewModelProviderFactory)[EditScheduleViewModel::class.java]

        val scheduleName = intent.getStringExtra("oldScheduleName").toString()
        val scheduleDescription = intent.getStringExtra("oldScheduleDescription").toString()
        val scheduleDaysPlannedOn = intent.getStringExtra("oldScheduleDaysPlannedOn").toString()

        binding.editScheduleEtName.setText(scheduleName)
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

            editScheduleViewModel.editSchedule(scheduleName, newScheduleName, newScheduleDescription, newScheduleDaysPlannedOn)
            finish()
        }

        binding.deleteScheduleButton.setOnClickListener {
            editScheduleViewModel.deleteSchedule(scheduleName)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}