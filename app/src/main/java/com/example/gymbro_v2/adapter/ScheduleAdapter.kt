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
    private val scheduleClickListener: (Schedule) -> Unit
) : ListAdapter<Schedule, ScheduleAdapter.ScheduleAdapterViewHolder>(DiffCallBack) {

    lateinit var context: Context
    class ScheduleAdapterViewHolder(
        private val binding: HomeRvMainListItemBinding,
        private val scheduleClickListener: (Schedule) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, schedule: Schedule) {
            binding.scheduleTitle.text = schedule.scheduleName
            binding.scheduleDescription.text = schedule.scheduleDescription
            val daysPlannedOn = schedule.scheduleDaysPlannedOn
            val sdf = SimpleDateFormat("EEE")
            val d = Date()
            val dayOfTheWeek: String = sdf.format(d)
            if (daysPlannedOn.contains(dayOfTheWeek)) {
                binding.scheduleConstraintLayout.setBackgroundColor(context.getColor(com.google.android.material.R.color.design_default_color_primary_variant))
            }
            binding.homeListItemParentLayout.setOnClickListener {
                scheduleClickListener(schedule)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapterViewHolder {
        context = parent.context
        return ScheduleAdapterViewHolder(HomeRvMainListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false),
            scheduleClickListener
        )
    }

    override fun onBindViewHolder(holder: ScheduleAdapterViewHolder, position: Int) {
        holder.bind(context, getItem(position))
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }
}