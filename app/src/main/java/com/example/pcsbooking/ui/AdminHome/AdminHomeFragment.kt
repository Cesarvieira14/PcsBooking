package com.example.pcsbooking.ui.AdminHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pcsbooking.databinding.FragmentAdminHomeBinding

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the summary data and update UI
        viewModel.summaryData.observe(viewLifecycleOwner) { summary ->
            binding.tvTotalMachines.text = "Total Machines: ${summary.totalMachines}"
            binding.tvActiveBookings.text = "Active Bookings: ${summary.activeBookings}"
            binding.tvTotalUsers.text = "Total Users: ${summary.totalUsers}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

