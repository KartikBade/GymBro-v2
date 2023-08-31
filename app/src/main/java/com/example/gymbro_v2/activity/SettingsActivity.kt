package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.ActivitySettingsBinding
import com.example.gymbro_v2.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rgBackupFrequency.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_backup_frequency_never -> { Toast.makeText(this, "NEVER!", Toast.LENGTH_SHORT).show() }
                R.id.rb_backup_frequency_per_day -> { Toast.makeText(this, "PER DAY!", Toast.LENGTH_SHORT).show() }
                R.id.rb_backup_frequency_per_week -> { Toast.makeText(this, "PER WEEK!", Toast.LENGTH_SHORT).show() }
            }
        }
        binding.btnBackupNow.setOnClickListener {
            Toast.makeText(this, "Data Backup Started!", Toast.LENGTH_SHORT).show()
        }
    }
}