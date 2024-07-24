package com.example.pcsbooking.ui.AdminBookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.FragmentAdminBookingsBinding

class AdminBookingsFragment : Fragment() {

    private var _binding: FragmentAdminBookingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminBookingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BookingAdapter(emptyList()) { bookingPair ->
            val (bookingId, booking) = bookingPair
            val userName = viewModel.getUserName(booking.userId) ?: "Unknown User"
            val intent = Intent(requireContext(), BookingDetailsActivity::class.java).apply {
                putExtra("BOOKING_ID", bookingId)
                putExtra("PC_ID", booking.pcId)
                putExtra("USER_NAME", userName)
                putExtra("DATE", booking.date)
                putExtra("START_TIME", booking.startTime)
                putExtra("END_TIME", booking.endTime)
            }
            startActivity(intent)
        }

        binding.bookingsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.bookingsRecyclerView.adapter = adapter

        viewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            adapter.updateBookings(bookings)
        }

        viewModel.users.observe(viewLifecycleOwner) { users ->
            // Ensure users LiveData is observed to make sure getUserName has the required data
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

