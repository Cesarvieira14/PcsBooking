package com.example.pcsbooking.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.R

class MyBookingsAdapter(
    private val context: Context,
    private var bookingsList: List<Booking>,
    private val onItemClick: (Booking) -> Unit
) : RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookingsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_booking_home, parent, false)
        return MyBookingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyBookingsViewHolder, position: Int) {
        val booking = bookingsList[position]
        holder.bind(booking)
        holder.itemView.setOnClickListener {
            onItemClick(booking)
        }
    }

    override fun getItemCount(): Int {
        return bookingsList.size
    }

    fun submitList(newList: List<Booking>) {
        bookingsList = newList
        notifyDataSetChanged()
    }

    inner class MyBookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val detailsText: TextView = itemView.findViewById(R.id.tv_booking_home_details)

        fun bind(booking: Booking) {
            val time = "${booking.startTime / 60}:00 - ${booking.endTime / 60}:00"
            detailsText.text = "${booking.pcId} - ${booking.date} at $time"
        }
    }
}
