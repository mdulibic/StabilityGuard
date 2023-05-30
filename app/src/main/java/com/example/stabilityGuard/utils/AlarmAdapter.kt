package com.example.stabilityGuard.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stabilityGuard.R
import com.example.stabilityGuard.databinding.ItemAlarmBinding
import com.example.stabilityGuard.model.Alarm

class AlarmAdapter(
    private var alarmList: ArrayList<Alarm> = arrayListOf(),
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alarm: Alarm) {
            binding.apply {
                this.tvId.text = binding.root.context.resources.getString(R.string.id_1_s, alarm.id)
                this.tvName.text =
                    binding.root.context.resources.getString(R.string.name_1_s, alarm.name)
                this.tvTimestamp.text = binding.root.context.resources.getString(
                    R.string.timestamp_1_s,
                    alarm.timestamp,
                )
                this.tvStatus.text =
                    binding.root.context.resources.getString(R.string.status_1_s, alarm.status.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding =
            ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = alarmList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    /**
     * Function to set new data to the adapter.
     * @param newData List of new data to be set.
     * @see [DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil)
     * for more info on how to optimize this.
     */

    fun setData(newData: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(newData)
        notifyDataSetChanged()
    }
}
