package com.example.stabilityGuard.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stabilityGuard.R
import com.example.stabilityGuard.databinding.ItemAlarmBinding
import com.example.stabilityGuard.model.Alarm
import com.example.stabilityGuard.model.AlarmStatus

class AlarmAdapter(
    private var alarmList: ArrayList<Alarm> = arrayListOf(),
    private val onItemAck: (String) -> Unit,
    private val onItemCleared: (String) -> Unit,
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var id: String? = null

        private val ackClickListener = View.OnClickListener {
            if (id == null) return@OnClickListener

            onItemAck(id!!)
        }

        private val clearClickListener = View.OnClickListener {
            if (id == null) return@OnClickListener

            onItemCleared(id!!)
        }

        fun bind(alarm: Alarm) {
            this.id = alarm.id
            binding.apply {
                this.tvId.text = binding.root.context.resources.getString(R.string.id_1_s, alarm.id)
                this.tvDeviceId.text = binding.root.context.resources.getString(R.string.device_id_1_s, alarm.deviceId)
                this.tvName.text =
                    binding.root.context.resources.getString(R.string.name_1_s, alarm.name)
                this.tvTimestamp.text = binding.root.context.resources.getString(
                    R.string.timestamp_1_s,
                    alarm.timestamp,
                )
                this.tvStatus.text =
                    binding.root.context.resources.getString(R.string.status_1_s, alarm.status.name)
            }
            binding.btnAcknowledge.setOnClickListener(ackClickListener)
            binding.btnClear.setOnClickListener(clearClickListener)
            when (alarm.status) {
                AlarmStatus.ACTIVE_ACK -> {
                    binding.btnAcknowledge.visibility = View.GONE
                    binding.btnClear.visibility = View.VISIBLE
                }

                AlarmStatus.ACTIVE_UNACK -> {
                    binding.btnAcknowledge.visibility = View.VISIBLE
                    binding.btnClear.visibility = View.VISIBLE
                }

                else -> {
                    binding.btnAcknowledge.visibility = View.GONE
                    binding.btnClear.visibility = View.GONE
                }
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
