package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gymbro_v2.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {

    lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleName = intent.getStringExtra("scheduleName").toString()
        title = scheduleName
    }
}