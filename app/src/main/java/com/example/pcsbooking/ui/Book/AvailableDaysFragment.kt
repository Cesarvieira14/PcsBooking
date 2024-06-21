package com.example.pcsbooking.ui.Book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentAvailableDaysBinding

class AvailableDaysFragment : Fragment() {

    private var _binding: FragmentAvailableDaysBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel

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

        // Observe available days
        bookViewModel.availableDays.observe(viewLifecycleOwner) { availableDays ->
            val daysAdapter = AvailableDaysAdapter(requireContext(), availableDays) { day ->
                // Set selected day in ViewModel
                bookViewModel.setSelectedDay(day)
                // Navigate to the next step, like TimeSlotFragment
                // Replace R.id.action_with_correct_action_id from your navigation graph
                findNavController().navigate(R.id.action_navigation_available_days_to_navigation_timeslot)
            }

            // Set up RecyclerView
            binding.daysRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = daysAdapter
            }
        }

        // Assuming that you have a selected PC, observe it to fetch available days for it
        bookViewModel.selectedPc.observe(viewLifecycleOwner) { selectedPc ->
            selectedPc?.let {
                bookViewModel.fetchAvailableDaysForPc(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
