package com.example.pcsbooking.ui.AdminBookings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.R

class BookingDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Retrieve the booking details from the Intent
        val bookingId = intent.getStringExtra("BOOKING_ID")
        val pcId = intent.getStringExtra("PC_ID")
        val userId = intent.getStringExtra("USER_ID")
        val date = intent.getStringExtra("DATE")
        val startTime = intent.getIntExtra("START_TIME", 0)
        val endTime = intent.getIntExtra("END_TIME", 0)

        // Populate the UI with the booking details
        // For example:
        findViewById<TextView>(R.id.tvPcId).text = pcId
        findViewById<TextView>(R.id.tvUserId).text = userId
        findViewById<TextView>(R.id.tvDate).text = date
        findViewById<TextView>(R.id.tvStartTime).text = startTime.toString()
        findViewById<TextView>(R.id.tvEndTime).text = endTime.toString()

        // Setup cancel button functionality
        findViewById<Button>(R.id.btnCancelBooking).setOnClickListener {
            // Implement cancel booking logic
            // For example, notify the ViewModel to cancel the booking
        }
    }
}