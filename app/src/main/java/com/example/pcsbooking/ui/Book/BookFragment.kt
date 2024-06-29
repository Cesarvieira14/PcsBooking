package com.example.pcsbooking.ui.Book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel
    private lateinit var pcAdapter: ArrayAdapter<String> // Adapter for ListView

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

        // Initialize ArrayAdapter for ListView
        pcAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arrayListOf())

        // Set adapter to ListView
        binding.pcsListView.adapter = pcAdapter

        // Observe PCs from ViewModel
        bookViewModel.pcs.observe(viewLifecycleOwner, Observer { pcs ->
            pcs?.let {
                pcAdapter.clear()
                pcAdapter.addAll(pcs.map { it.id }) // Assuming 'id' is a property of Pc class
                pcAdapter.notifyDataSetChanged()
            }
        })

        // Handle item click in ListView
        binding.pcsListView.setOnItemClickListener { parent, view, position, id ->
            val selectedPc = bookViewModel.pcs.value?.get(position)
            selectedPc?.let {
                bookViewModel.setSelectedPc(it)
                findNavController().navigate(R.id.action_bookFragment_to_availableDaysFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

