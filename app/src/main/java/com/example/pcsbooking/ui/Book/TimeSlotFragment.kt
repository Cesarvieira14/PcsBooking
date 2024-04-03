package com.example.pcsbooking.ui.Book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.FragmentTimeSlotBinding

class TimeSlotFragment : Fragment() {

    private lateinit var binding: FragmentTimeSlotBinding
    private lateinit var bookViewModel: BookViewModel
    private lateinit var timeSlotAdapter: TimeSlotAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeSlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        timeSlotAdapter = TimeSlotAdapter(listOf())

        binding.rvTimeSlots.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timeSlotAdapter
        }

        bookViewModel.selectedPc.observe(viewLifecycleOwner, Observer { pc ->
            // Update your UI to display the time slots for the selected PC
            timeSlotAdapter.updateTimeSlots(pc.reservations.values.toList())
        })
    }
}
