package com.example.pcsbooking.ui.AdminMachines

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.R
import com.google.firebase.database.FirebaseDatabase

class UpdateMachineActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var editLocation: EditText
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_machine)

        editName = findViewById(R.id.editName)
        editDescription = findViewById(R.id.editDescription)
        editLocation = findViewById(R.id.editLocation)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Get the data passed from the previous activity
        val name = intent.getStringExtra("machineName")
        val description = intent.getStringExtra("machineDescription")
        val location = intent.getStringExtra("machineLocation")

        // Set the data to the views
        editName.setText(name)
        editDescription.setText(description)
        editLocation.setText(location)

        btnUpdate.setOnClickListener {
            updateMachine()
        }
    }

    private fun updateMachine() {
        val newName = editName.text.toString()
        val newDescription = editDescription.text.toString()
        val newLocation = editLocation.text.toString()

        // Assume 'pcs' node in Firebase stores machines, and machine names are unique identifiers
        val database = FirebaseDatabase.getInstance().getReference("pcs")
        val machineUpdate = mapOf(
            "name" to newName,
            "description" to newDescription,
            "location" to newLocation
        )

        database.child(newName).updateChildren(machineUpdate).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update successful
                Toast.makeText(this, "Machine updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                // Update failed
                Toast.makeText(
                    this,
                    "Failed to update machine: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

