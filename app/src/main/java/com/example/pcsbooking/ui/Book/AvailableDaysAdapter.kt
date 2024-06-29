package com.example.pcsbooking.ui.Book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.R

class AvailableDaysAdapter(
    private val context: Context,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AvailableDaysAdapter.AvailableDayViewHolder>() {

    private var availableDays: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableDayViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_available_day, parent, false)
        return AvailableDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvailableDayViewHolder, position: Int) {
        val day = availableDays[position]
        holder.bind(day)
        holder.itemView.setOnClickListener {
            onItemClick(day)
        }
    }

    override fun getItemCount(): Int {
        return availableDays.size
    }

    fun submitList(days: List<String>) {
        availableDays = days
        notifyDataSetChanged()
    }

    inner class AvailableDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.tv_day)

        fun bind(day: String) {
            dayText.text = day
        }
    }
}


