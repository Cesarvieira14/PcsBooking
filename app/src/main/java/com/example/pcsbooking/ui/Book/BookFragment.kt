package com.example.pcsbooking.ui.Book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel
    private lateinit var pcAdapter: PcAdapter // Adapter for RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        // Initialize PcAdapter for RecyclerView
        pcAdapter = PcAdapter(requireContext(), listOf()) { selectedPc ->
            bookViewModel.setSelectedPc(selectedPc)
            findNavController().navigate(R.id.action_bookFragment_to_availableDaysFragment)
        }

        // Set adapter and layout manager to RecyclerView
        binding.pcsListView.layoutManager = LinearLayoutManager(requireContext())
        binding.pcsListView.adapter = pcAdapter

        // Observe PCs from ViewModel
        bookViewModel.pcs.observe(viewLifecycleOwner, Observer { pcs ->
            pcs?.let { pcAdapter.submitList(it) }

            if (pcs.isNotEmpty()) {
                binding.machinesProgressBar.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
