package com.example.pcsbooking.ui.AdminMachines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Machine
import com.example.pcsbooking.R

class MachineAdapter(
    private var machines: List<Machine>,
    private val onItemClick: (Machine) -> Unit // Lambda function to handle item clicks
) : RecyclerView.Adapter<MachineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvMachineName)
        val tvDescription: TextView = view.findViewById(R.id.tvMachineDescription)
        val tvLocation: TextView = view.findViewById(R.id.tvMachineLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_machine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val machine = machines[position]
        holder.tvName.text = machine.name
        holder.tvDescription.text = machine.description
        holder.tvLocation.text = machine.location

        // Set up the item click listener
        holder.itemView.setOnClickListener {
            onItemClick(machine)
        }
    }

    override fun getItemCount(): Int = machines.size

    fun updateMachines(newMachines: List<Machine>) {
        machines = newMachines
        notifyDataSetChanged()
    }
}

