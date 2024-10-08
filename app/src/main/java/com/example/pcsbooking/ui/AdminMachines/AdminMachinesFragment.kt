package com.example.pcsbooking.ui.AdminMachines

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.Model.Machine
import com.example.pcsbooking.databinding.FragmentAdminMachinesBinding

class AdminMachinesFragment : Fragment() {

    private var _binding: FragmentAdminMachinesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminMachinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMachinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with an empty list and set the click listener
        val adapter = MachineAdapter(emptyList()) { machine ->
            val intent = Intent(requireContext(), UpdateMachineActivity::class.java).apply {
                putExtra("machineId", machine.id)
                putExtra("machineName", machine.name)
                putExtra("machineDescription", machine.description)
                putExtra("machineLocation", machine.location)
            }
            startActivity(intent)
        }

        // Set up RecyclerView
        binding.machinesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.machinesRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel and update the RecyclerView
        viewModel.machines.observe(viewLifecycleOwner) { machines ->
            adapter.updateMachines(machines)
        }

        // Set up the Floating Action Button to navigate to CreateMachineActivity
        binding.btnAddMachine.setOnClickListener {
            val intent = Intent(requireContext(), CreateMachineActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
