package com.example.pcsbooking.ui.Book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private lateinit var bookViewModel: BookViewModel
    private lateinit var pcAdapter: PcAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        // Instantiate PcAdapter with the click listener
        pcAdapter = PcAdapter(listOf()) { pc ->
            bookViewModel.selectedPc.value = pc
            // Navigate to TimeSlotFragment
            val action = BookFragmentDirections.actionBookFragmentToTimeSlotFragment()
            view.findNavController().navigate(action)
        }

        binding.rvPcs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pcAdapter
        }

        bookViewModel.pcs.observe(viewLifecycleOwner, Observer { pcs ->
            pcAdapter.updatePcs(pcs)
        })
    }
}
