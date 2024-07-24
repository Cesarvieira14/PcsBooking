package com.example.pcsbooking.ui.AdminBookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.databinding.FragmentAdminBookingsBinding
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class AdminBookingsFragment : Fragment() {

    private var _binding: FragmentAdminBookingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminBookingsViewModel by viewModels()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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

            val intent = Intent(requireContext(), BookingDetailsActivity::class.java).apply {
                putExtra("BOOKING_ID", bookingId)
                putExtra("PC_ID", booking.pcId)
                putExtra("USER_ID", booking.userId)  // Pass USER_ID
                putExtra("DATE", booking.date)
                putExtra("START_TIME", booking.startTime)
                putExtra("END_TIME", booking.endTime)
            }
            startActivity(intent)
        }

        binding.bookingsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.bookingsRecyclerView.adapter = adapter

        viewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            updateBookings(adapter, bookings)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    updateBookings(adapter, viewModel.bookings.value ?: emptyList())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateBookings(adapter: BookingAdapter, bookings: List<Pair<String, Booking>>) {
        val todayDate = getTodayDate()
        val nextWeekDate = getNextWeekDate()
        val filteredBookings = when (binding.tabLayout.selectedTabPosition) {
            0 -> bookings.filter { it.second.date == todayDate }
            1 -> bookings.filter { it.second.date >= todayDate && it.second.date <= nextWeekDate }
            2 -> bookings.filter { isThisWeek(it.second.date) }
            else -> bookings
        }
        adapter.updateBookings(filteredBookings)
    }

    private fun getTodayDate(): String {
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

    private fun getNextWeekDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return dateFormat.format(calendar.time)
    }

    private fun isThisWeek(dateString: String): Boolean {
        val date = dateFormat.parse(dateString) ?: return false
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val bookingWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val bookingYear = calendar.get(Calendar.YEAR)

        return currentWeek == bookingWeek && currentYear == bookingYear
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
