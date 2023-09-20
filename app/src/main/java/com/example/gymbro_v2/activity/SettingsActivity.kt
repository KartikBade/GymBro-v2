package com.example.gymbro_v2.activity

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gymbro_v2.R
import com.example.gymbro_v2.broadcastreceiver.BackupNotificationReceiver
import com.example.gymbro_v2.databinding.ActivitySettingsBinding
import com.example.gymbro_v2.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rgBackupFrequency.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_backup_frequency_never -> { settingsViewModel.setWorkerMode(0) }
                R.id.rb_backup_frequency_per_day -> { settingsViewModel.setWorkerMode(1) }
                R.id.rb_backup_frequency_per_week -> { settingsViewModel.setWorkerMode(2) }
            }
        }
        binding.btnBackupNow.setOnClickListener {
            settingsViewModel.setWorkerMode(3)
        }
    }
}