package com.example.gymbro_v2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.HomeRvMainListItemBinding
import com.example.gymbro_v2.model.Exercise

class ExerciseAdapter(private val checkboxClickListener: (Exercise) -> Unit): ListAdapter<Exercise, ExerciseAdapter.ExerciseAdapterViewHolder>(DiffCallBack) {

    class ExerciseAdapterViewHolder(
        val binding: HomeRvMainListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.homeListItemTvExerciseTitle.text = exercise.name
            binding.homeListItemTvExerciseDescription.text = exercise.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapterViewHolder {
        return ExerciseAdapterViewHolder(HomeRvMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExerciseAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.homeListItemCheckBox.setOnClickListener {
            checkboxClickListener(getItem(position))
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }
}