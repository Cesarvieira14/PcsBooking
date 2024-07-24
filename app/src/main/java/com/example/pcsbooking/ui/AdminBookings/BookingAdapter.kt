package com.example.pcsbooking.ui.AdminBookings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.R

class BookingAdapter(
    private var bookings: List<Pair<String, Booking>>,
    private val onClick: (Pair<String, Booking>) -> Unit
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPcId: TextView = view.findViewById(R.id.tvPcId)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvEndTime: TextView = view.findViewById(R.id.tvEndTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (bookingId, booking) = bookings[position]

        // Fetch the user name from the ViewModel if available
        val userName = "Unknown User" // Replace with actual user name fetching logic

        holder.tvPcId.text = "PC ID: ${booking.pcId}"
        holder.tvUserName.text = "User: $userName"
        holder.tvDate.text = "Date: ${booking.date}"
        holder.tvStartTime.text = "Start Time: ${convertMinutesToHours(booking.startTime)}"
        holder.tvEndTime.text = "End Time: ${convertMinutesToHours(booking.endTime)}"

        holder.itemView.setOnClickListener { onClick(Pair(bookingId, booking)) }
    }

    override fun getItemCount(): Int = bookings.size

    fun updateBookings(newBookings: List<Pair<String, Booking>>) {
        bookings = newBookings
        notifyDataSetChanged()
    }

    private fun convertMinutesToHours(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%02d:%02d", hours, mins)
    }
}
