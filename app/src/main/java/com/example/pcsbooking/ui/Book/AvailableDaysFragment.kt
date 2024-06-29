package com.example.pcsbooking.ui.Book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentAvailableDaysBinding

class AvailableDaysFragment : Fragment() {

    private var _binding: FragmentAvailableDaysBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel
    private lateinit var daysAdapter: AvailableDaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvailableDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Initialize RecyclerView and Adapter for available days
        daysAdapter = AvailableDaysAdapter(requireContext()) { day ->
            // Handle day selection
            bookViewModel.setSelectedDay(day)
            findNavController().navigate(R.id.action_navigation_available_days_to_navigation_timeslot)
        }

        binding.daysRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = daysAdapter
        }

        // Observe available days from ViewModel
        bookViewModel.availableDays.observe(viewLifecycleOwner) { availableDays ->
            // Update the list of available days in the adapter
            daysAdapter.submitList(availableDays)
        }

        // Fetch available days when fragment is created
        bookViewModel.fetchAvailableDays()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

