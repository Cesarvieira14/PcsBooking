package com.example.pcsbooking.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentHomeBinding
import com.example.pcsbooking.ui.Book.BookViewModel
import com.example.pcsbooking.ui.Book.PcAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        handleMyBookingsAdapter(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleMyBookingsAdapter(view: View) {
        myBookingsAdapter = MyBookingsAdapter(requireContext(), listOf()) { selectedPc ->
            Toast.makeText(requireContext(), "Clicked on my booking", Toast.LENGTH_SHORT).show()
        }

        binding.rvCurrentBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrentBookings.adapter = myBookingsAdapter

        bookViewModel.myBookings.observe(viewLifecycleOwner, Observer { bookings ->
            bookings?.let { myBookingsAdapter.submitList(it) }

            if (bookings.isNotEmpty()) {
                binding.myBookingsProgressBar.visibility = View.GONE
            }
        })
    }
}