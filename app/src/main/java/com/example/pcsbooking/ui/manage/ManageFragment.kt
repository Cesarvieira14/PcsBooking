package com.example.pcsbooking.ui.manage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentManageBinding

class ManageFragment : Fragment() {

    private var _binding: FragmentManageBinding? = null
    private val binding get() = _binding!!
    private lateinit var manageViewModel: ManageViewModel
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageViewModel = ViewModelProvider(this).get(ManageViewModel::class.java)

        bookingAdapter = BookingAdapter(emptyList()) { booking ->
            manageViewModel.unbook(booking)
        }

        binding.rvBookings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookingAdapter
        }

        manageViewModel.futureBookings.observe(viewLifecycleOwner) { bookings ->
            bookingAdapter.updateBookings(bookings)
        }

        manageViewModel.fetchUserBookings()

        binding.btnPastBookings.setOnClickListener {
            val intent = Intent(requireContext(), PastBookingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
