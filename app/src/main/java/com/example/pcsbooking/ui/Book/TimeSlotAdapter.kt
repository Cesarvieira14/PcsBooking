package com.example.pcsbooking.ui.Book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.TimeSlot
import com.example.pcsbooking.R

class TimeSlotAdapter(private var timeSlots: List<TimeSlot>) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    class TimeSlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTimeSlot: TextView = view.findViewById(R.id.tv_time_slot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]
        holder.tvTimeSlot.text = timeSlot.time
    }

    override fun getItemCount() = timeSlots.size

    fun updateTimeSlots(timeSlots: List<TimeSlot>) {
        this.timeSlots = timeSlots
        notifyDataSetChanged()
    }
}
