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

        // Observe PCs
        bookViewModel.pcs.observe(viewLifecycleOwner) { pcsList ->
            val pcListAdapter = PcListAdapter(requireContext(), layoutInflater, pcsList)
            binding.pcsListView.adapter = pcListAdapter

            // Handle item clicks in the list view
            binding.pcsListView.setOnItemClickListener { adapterView, _, position, _ ->
                // Retrieve the clicked PC object from the list using the position
                val pc = pcsList[position]
                // Set selected PC in ViewModel
                bookViewModel.setSelectedPc(pc)
                // Fetch available days for the selected PC
                bookViewModel.fetchAvailableDaysForPc(pc)
            }
        }

        // Observe available days for the selected PC
        bookViewModel.availableDays.observe(viewLifecycleOwner) { availableDays ->
            // Navigate to AvailableDaysFragment passing the available days
            val action = BookFragmentDirections.actionBookFragmentToAvailableDaysFragment()
            view.findNavController().navigate(action)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

