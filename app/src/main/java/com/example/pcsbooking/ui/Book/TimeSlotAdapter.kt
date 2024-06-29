package com.example.pcsbooking.ui.Book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.R

class TimeSlotAdapter(
    private val context: Context,
    private var timeSlots: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]
        holder.bind(timeSlot)
        holder.itemView.setOnClickListener {
            onItemClick(timeSlot)
        }
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    fun submitList(newList: List<String>) {
        timeSlots = newList
        notifyDataSetChanged()
    }

    inner class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeSlotText: TextView = itemView.findViewById(R.id.tv_time_slot)

        fun bind(timeSlot: String) {
            timeSlotText.text = timeSlot
        }
    }
}
