package com.example.pcsbooking.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.FragmentHomeBinding
import com.example.pcsbooking.ui.Book.BookViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var bookViewModel: BookViewModel
    private lateinit var myBookingsAdapter: MyBookingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        homeViewModel.welcomeMessage.observe(viewLifecycleOwner, Observer { message ->
            binding.tvWelcomeMessage.text = message
        })

        homeViewModel.nextBooking.observe(viewLifecycleOwner, Observer { booking ->
            if (booking != null) {
                binding.tvNextBooking.text =
                    "Your next booking is on ${booking.date} from ${booking.startTime / 60}:00 to ${booking.endTime / 60}:00."
            } else {
                binding.tvNextBooking.text = "You have no upcoming bookings."
            }
        })

        handleMyBookingsAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleMyBookingsAdapter() {
        myBookingsAdapter = MyBookingsAdapter(requireContext(), listOf()) { selectedBooking ->
            Toast.makeText(
                requireContext(),
                "To Manage go to the manage section",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvCurrentBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrentBookings.adapter = myBookingsAdapter

        homeViewModel.upcomingBookings.observe(viewLifecycleOwner, Observer { bookings ->
            bookings?.let { myBookingsAdapter.submitList(it) }

            if (bookings.isNotEmpty()) {
                binding.myBookingsProgressBar.visibility = View.GONE
            }
        })
    }
}

