package com.example.pcsbooking.ui.manage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.ActivityPastBookingsBinding

class PastBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPastBookingsBinding
    private val manageViewModel: ManageViewModel by viewModels()
    private lateinit var pastBookingAdapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pastBookingAdapter = BookingAdapter(emptyList()) {}

        binding.rvPastBookings.apply {
            layoutManager = LinearLayoutManager(this@PastBookingsActivity)
            adapter = pastBookingAdapter
        }

        manageViewModel.pastBookings.observe(this, Observer { bookings ->
            pastBookingAdapter.updateBookings(bookings)
        })

        manageViewModel.fetchUserBookings()
    }
}
