package com.example.pcsbooking.ui.AdminMachines

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateMachineActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var editLocation: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var machineId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_machine)

        editName = findViewById(R.id.editName)
        editDescription = findViewById(R.id.editDescription)
        editLocation = findViewById(R.id.editLocation)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        machineId = intent.getStringExtra("machineId") ?: return
        val name = intent.getStringExtra("machineName")
        val description = intent.getStringExtra("machineDescription")
        val location = intent.getStringExtra("machineLocation")

        editName.setText(name)
        editDescription.setText(description)
        editLocation.setText(location)

        btnUpdate.setOnClickListener {
            updateMachine()
        }

        btnDelete.setOnClickListener {
            deleteMachine()
        }
    }

    private fun updateMachine() {
        val newName = editName.text.toString()
        val newDescription = editDescription.text.toString()
        val newLocation = editLocation.text.toString()

        val database = FirebaseDatabase.getInstance().getReference("pcs")
        val machineUpdate = mapOf(
            "name" to newName,
            "description" to newDescription,
            "location" to newLocation
        )

        database.child(machineId).updateChildren(machineUpdate).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Machine updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(
                    this,
                    "Failed to update machine: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun deleteMachine() {
        val database = FirebaseDatabase.getInstance().getReference("pcs")

        database.child(machineId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Machine deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(
                    this,
                    "Failed to delete machine: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
