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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog

class TimeSlotFragment : Fragment() {

    private var _binding: FragmentTimeSlotBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel
    private lateinit var timeSlotAdapter: TimeSlotAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeSlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Initialize RecyclerView and Adapter for time slots
        timeSlotAdapter = TimeSlotAdapter(requireContext(), emptyList()) { timeSlot ->
            // Handle time slot selection and booking
            val pcId = bookViewModel.selectedPc.value?.id ?: ""
            val selectedDay = bookViewModel.selectedDay.value ?: ""
            val startTime = timeSlot.substringBefore("-").trim()
            val endTime = timeSlot.substringAfter("-").trim()

            // Convert time to minutes for database storage
            val startTimeInMinutes = startTime.split(":")[0].toInt() * 60
            val endTimeInMinutes = endTime.split(":")[0].toInt() * 60

            // Show confirmation dialog
            showConfirmationDialog(pcId, selectedDay, startTimeInMinutes, endTimeInMinutes)
        }

        binding.rvTimeSlots.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timeSlotAdapter
        }

        // Observe available time slots from ViewModel
        bookViewModel.availableTimeSlots.observe(
            viewLifecycleOwner,
            Observer { availableTimeSlots ->
                timeSlotAdapter.submitList(availableTimeSlots)
            })

        // Fetch available time slots for the selected day and PC
        bookViewModel.selectedDay.observe(viewLifecycleOwner, Observer { selectedDay ->
            val selectedPc = bookViewModel.selectedPc.value
            if (selectedPc != null) {
                bookViewModel.fetchAvailableTimeSlots(selectedDay, selectedPc.id)
            }
        })
    }

    private fun showConfirmationDialog(
        pcId: String,
        selectedDay: String,
        startTimeInMinutes: Int,
        endTimeInMinutes: Int
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Booking")
        builder.setMessage("Do you want to book this time slot?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            // Perform booking
            bookViewModel.bookPc(
                pcId,
                Firebase.auth.currentUser?.uid ?: "",
                selectedDay,
                startTimeInMinutes,
                endTimeInMinutes
            )
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
