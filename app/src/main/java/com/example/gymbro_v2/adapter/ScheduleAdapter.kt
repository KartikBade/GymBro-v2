package com.example.gymbro_v2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbro_v2.R
import com.example.gymbro_v2.database.entities.Schedule
import com.example.gymbro_v2.databinding.HomeRvMainListItemBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(
    private val context: Context,
    private val editScheduleClickListener: (Schedule) -> Unit,
    private val scheduleClickListener: (Schedule) -> Unit): ListAdapter<Schedule, ScheduleAdapter.ScheduleAdapterViewHolder>(DiffCallBack) {

    class ScheduleAdapterViewHolder(
        val binding: HomeRvMainListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, schedule: Schedule) {
            binding.homeListItemTvScheduleTitle.text = schedule.scheduleName
            binding.homeListItemTvScheduleDescription.text = schedule.scheduleDescription
            val daysPlannedOn = schedule.scheduleDaysPlannedOn
            if (daysPlannedOn.contains("Mon")) { binding.monday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Tue")) { binding.tuesday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Wed")) { binding.wednesday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Thu")) { binding.thursday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Fri")) { binding.friday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Sat")) { binding.saturday.setTextColor(context.getColor(R.color.white)) }
            if (daysPlannedOn.contains("Sun")) { binding.sunday.setTextColor(context.getColor(R.color.white)) }
            binding.homeListItemTvTotalExercisesTitle.text = context.getString(R.string.total_exercises)
            binding.homeListItemTvTotalExercises.text = schedule.scheduleTotalExercises.toString()
            val sdf = SimpleDateFormat("EEE")
            val d = Date()
            val dayOfTheWeek: String = sdf.format(d)
            if (daysPlannedOn.contains(dayOfTheWeek)) {
                binding.homeListItemTvScheduleTitle.setBackgroundColor(context.getColor(R.color.purple_500))
                binding.homeListItemTvScheduleDescription.setTextColor(context.getColor(R.color.black))
                binding.homeListItemTvTotalExercisesTitle.setTextColor(context.getColor(R.color.black))
                binding.homeListItemTvTotalExercises.setTextColor(context.getColor(R.color.black))
                binding.homeListItemLinearParent.setBackgroundColor(context.getColor(R.color.purple_500))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapterViewHolder {
        return ScheduleAdapterViewHolder(HomeRvMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ScheduleAdapterViewHolder, position: Int) {
        holder.bind(context, getItem(position))
        holder.binding.ivEditSchedule.setOnClickListener {
            editScheduleClickListener(getItem(position))
        }
        holder.binding.homeListItemConstraintParent.setOnClickListener {
            scheduleClickListener(getItem(position))
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleDescription == newItem.scheduleDescription && oldItem.scheduleDaysPlannedOn == newItem.scheduleDaysPlannedOn && oldItem.scheduleTotalExercises == newItem.scheduleTotalExercises
        }
    }
}