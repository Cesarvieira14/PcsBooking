package com.example.pcsbooking.Model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PcRepository {
    private val database = FirebaseDatabase.getInstance().getReference()

    val pcs = MutableLiveData<List<Pc>>()

    init {
        fetchPcs()
    }

    private fun fetchPcs() {
        database.child("reservations").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pcs = mutableListOf<Pc>()
                dataSnapshot.children.forEach { pcSnapshot ->
                    val reservations = mutableMapOf<String, TimeSlot>()
                    pcSnapshot.children.forEach { dateSnapshot ->
                        dateSnapshot.children.forEach { timeSlotSnapshot ->
                            val timeSlot = timeSlotSnapshot.getValue(TimeSlot::class.java)
                            if (timeSlot != null) {
                                reservations["${dateSnapshot.key} ${timeSlotSnapshot.key}"] = timeSlot
                            }
                        }
                    }
                    pcs.add(Pc(pcSnapshot.key ?: "", reservations))
                }
                this@PcRepository.pcs.value = pcs
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    fun bookPc(pcId: String, timeSlot: String) {
        // Update the Firebase database to book the PC
        val reservationRef = database.child("reservations").child(pcId).child(timeSlot)
        val reservation = mapOf("booked" to true, "bookedBy" to FirebaseAuth.getInstance().currentUser?.uid)
        reservationRef.setValue(reservation)
    }
}
