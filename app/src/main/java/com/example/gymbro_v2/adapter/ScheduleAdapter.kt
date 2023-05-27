package com.example.gymbro_v2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.HomeRvMainListItemBinding
import com.example.gymbro_v2.model.Schedule
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(private val context: Context, private val checkboxClickListener: (Schedule) -> Unit): ListAdapter<Schedule, ScheduleAdapter.ExerciseAdapterViewHolder>(DiffCallBack) {

    class ExerciseAdapterViewHolder(
        val binding: HomeRvMainListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, schedule: Schedule) {
            binding.homeListItemTvExerciseTitle.text = schedule.name
            binding.homeListItemTvExerciseDescription.text = schedule.description
            val daysPlannedOn = schedule.daysPlannedOn
            if (daysPlannedOn.contains("Mon")) { binding.monday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Tue")) { binding.tuesday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Wed")) { binding.wednesday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Thu")) { binding.thursday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Fri")) { binding.friday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Sat")) { binding.saturday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Sun")) { binding.sunday.setTextColor(context.getColor(R.color.white)) }
            binding.homeListItemTvTotalExercises.text = context.getString(R.string.total_exercises, schedule.totalExercises)
            val sdf = SimpleDateFormat("EEE")
            val d = Date()
            val dayOfTheWeek: String = sdf.format(d)
            if (daysPlannedOn.contains(dayOfTheWeek)) {
                binding.homeListItemTvExerciseTitle.setBackgroundColor(context.getColor(R.color.purple_500))
                binding.homeListItemTvExerciseDescription.setTextColor(context.getColor(R.color.black))
                binding.homeListItemTvTotalExercises.setTextColor(context.getColor(R.color.black))
                binding.homeListItemLinearParent.setBackgroundColor(context.getColor(R.color.purple_500))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapterViewHolder {
        return ExerciseAdapterViewHolder(HomeRvMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExerciseAdapterViewHolder, position: Int) {
        holder.bind(context, getItem(position))
        holder.binding.homeListItemConstraintParent.setOnClickListener {
            checkboxClickListener(getItem(position))
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }
}