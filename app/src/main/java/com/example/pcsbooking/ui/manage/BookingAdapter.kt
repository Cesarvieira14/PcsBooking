package com.example.pcsbooking.ui.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.R
import java.text.SimpleDateFormat
import java.util.*

class BookingAdapter(
    private var bookings: List<Booking>,
    private val onUnbookClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view, onUnbookClick)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount() = bookings.size

    fun updateBookings(newBookings: List<Booking>) {
        bookings = newBookings
        notifyDataSetChanged()
    }

    class BookingViewHolder(itemView: View, private val onUnbookClick: (Booking) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvBookingDetails: TextView = itemView.findViewById(R.id.tvBookingDetails)
        private val btnUnbook: Button = itemView.findViewById(R.id.btnUnbook)

        fun bind(booking: Booking) {
            val details =
                "PC: ${booking.pcId}\nDate: ${booking.date}\nTime: ${booking.startTime} - ${booking.endTime}"
            tvBookingDetails.text = details
            btnUnbook.visibility = if (isFutureBooking(booking)) View.VISIBLE else View.GONE
            btnUnbook.setOnClickListener {
                onUnbookClick(booking)
            }
        }

        private fun isFutureBooking(booking: Booking): Boolean {
            val bookingDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(booking.date)
            return bookingDate?.after(Date()) == true
        }
    }
}
