package com.example.pcsbooking.ui.Book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.Model.PcRepository
import com.google.firebase.database.*

class BookViewModel : ViewModel() {
    private val repository = PcRepository()
    val pcs = repository.pcs

    // Define LiveData for available days
    private val _availableDays = MutableLiveData<List<String>>()
    val availableDays: LiveData<List<String>> get() = _availableDays

    // Define LiveData for selected day
    private val _selectedDay = MutableLiveData<String>()
    val selectedDay: LiveData<String> get() = _selectedDay

    // Define LiveData for selected PC
    private val _selectedPc = MutableLiveData<Pc>()
    val selectedPc: LiveData<Pc> get() = _selectedPc

    private val database = FirebaseDatabase.getInstance()
    private val reservationsRef = database.getReference("reservations")

    fun bookPc(pcId: String, timeSlot: String) {
        repository.bookPc(pcId, timeSlot)
    }

    fun fetchAvailableDays() {
        val availableDaysList = mutableListOf<String>()

        reservationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before updating
                availableDaysList.clear()

                // Iterate over the children of reservations node
                for (pcSnapshot in snapshot.children) {
                    // Get the dates under each pc
                    for (dateSnapshot in pcSnapshot.children) {
                        // Add the date to the list
                        availableDaysList.add(dateSnapshot.key.toString())
                    }
                }

                // Update the LiveData with the list of available days
                _availableDays.value = availableDaysList.distinct() // Remove duplicates
            }

            override fun onCancelled(error: DatabaseError) {
                // Clear the list in case of cancellation
                availableDaysList.clear()
                // Handle error
            }
        })
    }
    fun fetchAvailableDaysForPc(pc: Pc) {
        val availableDaysList = mutableListOf<String>()

        reservationsRef.child(pc.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before updating
                availableDaysList.clear()

                // Iterate over the children of the selected PC node
                for (dateSnapshot in snapshot.children) {
                    // Add the date to the list
                    availableDaysList.add(dateSnapshot.key.toString())
                }

                // Update the LiveData with the list of available days for the selected PC
                _availableDays.value = availableDaysList.distinct() // Remove duplicates
            }

            override fun onCancelled(error: DatabaseError) {
                // Clear the list in case of cancellation
                availableDaysList.clear()
                // Handle error
            }
        })
    }

    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun setSelectedPc(pc: Pc) {
        _selectedPc.value = pc
    }
}

