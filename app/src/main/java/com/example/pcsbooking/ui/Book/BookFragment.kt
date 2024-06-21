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
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.databinding.FragmentBookBinding
import com.example.pcsbooking.ui.PcList.PcListAdapter

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookViewModel: BookViewModel

    private lateinit var pcAdapter: PcAdapter

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

        bookViewModel.pcs.observe(viewLifecycleOwner) { pcsList ->
            val pcListAdapter = PcListAdapter(requireContext(), layoutInflater, pcsList)
            binding.pcsListView.adapter = pcListAdapter

            binding.pcsListView.setOnItemClickListener { _, _, position, _ ->
                val pc = pcsList[position]
                bookViewModel.setSelectedPc(pc)
                // Trigger navigation when a PC is selected
                val action = BookFragmentDirections.actionBookFragmentToAvailableDaysFragment()
                view.findNavController().navigate(action)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

