package com.example.gymbro_v2.activity

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
            finish()
        }
    }
}