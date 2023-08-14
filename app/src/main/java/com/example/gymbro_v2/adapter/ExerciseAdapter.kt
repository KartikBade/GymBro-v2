package com.example.gymbro_v2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbro_v2.database.entities.Exercise
import com.example.gymbro_v2.databinding.ScheduleRvListItemBinding

class ExerciseAdapter(
    private val exerciseClickListener: (Exercise) -> Unit
): ListAdapter<Exercise, ExerciseAdapter.ExerciseAdapterViewHolder>(DiffCallBack) {

    class ExerciseAdapterViewHolder(
        val binding: ScheduleRvListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: Exercise) {
            binding.scheduleRvListItemExerciseTitle.text = exercise.exerciseName
            binding.scheduleRvListItemExerciseInstructions.text = exercise.exerciseInstructions
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapterViewHolder {
        return ExerciseAdapterViewHolder(ScheduleRvListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExerciseAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.parentConstraintLayout.setOnClickListener {
            exerciseClickListener(getItem(position))
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseId == newItem.exerciseId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

    }
}