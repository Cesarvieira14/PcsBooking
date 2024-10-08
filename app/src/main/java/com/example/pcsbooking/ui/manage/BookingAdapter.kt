package com.example.pcsbooking.ui.manage

import android.app.AlertDialog
import android.content.Context
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

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view, onUnbookClick, context)
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

    class BookingViewHolder(
        itemView: View,
        private val onUnbookClick: (Booking) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvBookingDetails: TextView = itemView.findViewById(R.id.tvBookingDetails)
        private val btnUnbook: Button = itemView.findViewById(R.id.btnUnbook)

        fun bind(booking: Booking) {
            val details =
                "PC: ${booking.pcId}\nDate: ${booking.date}\nTime: ${formatTime(booking.startTime)} - ${
                    formatTime(booking.endTime)
                }"
            tvBookingDetails.text = details
            btnUnbook.visibility = if (isFutureBooking(booking)) View.VISIBLE else View.GONE
            btnUnbook.setOnClickListener {
                showConfirmationDialog(booking)
            }
        }

        private fun showConfirmationDialog(booking: Booking) {
            AlertDialog.Builder(context)
                .setTitle("Confirm Unbooking")
                .setMessage("Are you sure you want to unbook this time slot?")
                .setPositiveButton("Yes") { _, _ ->
                    onUnbookClick(booking)
                }
                .setNegativeButton("No", null)
                .show()
        }

        private fun formatTime(minutes: Int): String {
            val hours = minutes / 60
            val mins = minutes % 60
            return String.format("%02d:%02d", hours, mins)
        }

        private fun isFutureBooking(booking: Booking): Boolean {
            val bookingDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(booking.date)
            return bookingDate?.after(Date()) == true
        }
    }
}
