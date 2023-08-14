package com.example.gymbro_v2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbro_v2.database.entities.Log
import com.example.gymbro_v2.databinding.LogListItemBinding

class LogAdapter(
    private val logClickListener: (Log) -> Unit
): ListAdapter<Log, LogAdapter.LogViewHolder>(DiffCallBack) {

    class LogViewHolder(
        val binding: LogListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(log: Log, setCount: Int) {
            binding.tvSet.text = setCount.toString()
            binding.tvReps.text = log.reps.toString()
            binding.tvWeight.text = log.weight.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(LogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(getItem(position), position+1)
        holder.binding.logLinearLayout.setOnClickListener {
            logClickListener(getItem(position))
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Log>() {
        override fun areItemsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem.logId == newItem.logId
        }

        override fun areContentsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem == newItem
        }

    }
}