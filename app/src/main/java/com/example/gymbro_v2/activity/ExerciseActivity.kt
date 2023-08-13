package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gymbro_v2.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCompleteSet.setOnClickListener {
            val weightString = binding.etWeight.text.toString().trim()
            val repsString = binding.etReps.text.toString().trim()

            try {
                val weight = weightString.toInt()
                val reps = repsString.toInt()
                Toast.makeText(this, "Set completed: $reps reps with $weight weight.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid values entered", Toast.LENGTH_SHORT).show()
            }
        }
    }
}