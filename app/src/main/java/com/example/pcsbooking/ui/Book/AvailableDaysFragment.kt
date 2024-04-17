package com.example.pcsbooking.ui.Book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Fetch available days only if the LiveData is not already observed
        if (bookViewModel.availableDays.value == null) {
            bookViewModel.fetchAvailableDays()
        }

        // Observe available days
        bookViewModel.availableDays.observe(viewLifecycleOwner) { availableDays ->
            val daysAdapter = AvailableDaysAdapter(requireContext(), availableDays) { day ->
                // Set selected day in ViewModel
                bookViewModel.setSelectedDay(day)
                // Navigate back to BookFragment
                findNavController().popBackStack()
            }

            // Set up RecyclerView
            binding.daysRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = daysAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

