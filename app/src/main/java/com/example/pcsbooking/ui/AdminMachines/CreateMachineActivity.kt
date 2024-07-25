package com.example.pcsbooking.ui.AdminMachines

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.Model.Machine
import com.example.pcsbooking.R
import com.google.firebase.database.FirebaseDatabase

class CreateMachineActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_machine)

        etName = findViewById(R.id.etMachineName)
        etDescription = findViewById(R.id.etMachineDescription)
        etLocation = findViewById(R.id.etMachineLocation)
        btnSave = findViewById(R.id.btnSaveMachine)

        btnSave.setOnClickListener {
            saveMachine()
        }
    }

    private fun saveMachine() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val location = etLocation.text.toString().trim()

        if (name.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()) {
            val machineId = FirebaseDatabase.getInstance().reference.child("pcs").push().key
            val machine = Machine(name, description, location)

            if (machineId != null) {
                FirebaseDatabase.getInstance().reference.child("pcs").child(machineId)
                    .setValue(machine)
                    .addOnSuccessListener {
                        finish() // Close the activity
                    }
                    .addOnFailureListener {
                        // Handle the error
                    }
            }
        }
    }
}
