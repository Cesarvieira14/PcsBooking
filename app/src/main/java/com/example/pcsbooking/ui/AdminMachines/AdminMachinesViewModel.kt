package com.example.pcsbooking.ui.AdminMachines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Machine
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminMachinesViewModel : ViewModel() {

    private val _machines = MutableLiveData<List<Machine>>()
    val machines: LiveData<List<Machine>> = _machines

    init {
        fetchMachines()
    }

    private fun fetchMachines() {
        val database = FirebaseDatabase.getInstance().getReference("pcs")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val machinesList = mutableListOf<Machine>()
                for (machineSnapshot in snapshot.children) {
                    val machine = machineSnapshot.getValue(Machine::class.java)
                    if (machine != null) {
                        // Include ID in the machine object
                        machinesList.add(machine.copy(id = machineSnapshot.key!!))
                    }
                }
                _machines.value = machinesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun updateMachine(machine: Machine) {
        val database = FirebaseDatabase.getInstance().getReference("pcs")
        database.child(machine.id).setValue(machine).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Optionally notify the UI or log the success
            } else {
                // Handle the failure
            }
        }
    }
}
