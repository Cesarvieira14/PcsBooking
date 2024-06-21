package com.example.pcsbooking.ui.Book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.Model.PcRepository
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BookViewModel : ViewModel() {
    private val repository = PcRepository()
    val pcs = repository.pcs

    private val _availableDays = MutableLiveData<List<String>>()
    val availableDays: LiveData<List<String>> get() = _availableDays

    private val _selectedDay = MutableLiveData<String>()
    val selectedDay: LiveData<String> get() = _selectedDay

    private val _selectedPc = MutableLiveData<Pc>()
    val selectedPc: LiveData<Pc> get() = _selectedPc

    private val database = FirebaseDatabase.getInstance()
    private val reservationsRef = database.getReference("reservations")

    init {
        updateBookingAvailability()
    }

    fun bookPc(pcId: String, timeSlot: String) {
        repository.bookPc(pcId, timeSlot)
    }

    private fun updateBookingAvailability() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val todayStr = dateFormat.format(calendar.time)

        reservationsRef.child(todayStr).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reservationsRef.child(todayStr).removeValue()
                    addNextWeekDate(calendar, dateFormat)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun addNextWeekDate(calendar: Calendar, dateFormat: SimpleDateFormat) {
        calendar.add(Calendar.DATE, 7)
        val nextWeekStr = dateFormat.format(calendar.time)

        // Construct a map of the new day with time slots
        val timeSlots = mutableMapOf<String, Boolean>().apply {
            for (hour in 9..17) {
                this["$hour:00-${hour + 1}:00"] = false
            }
        }
        reservationsRef.child(nextWeekStr).setValue(timeSlots)
    }

     fun fetchAvailableDaysForPc(pc: Pc) {
        val availableDaysList = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val todayStr = dateFormat.format(calendar.time)

        reservationsRef.child(pc.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                availableDaysList.clear()

                // Debug: Print out the keys to see what we are getting.
                println("Keys: ${snapshot.children.map { it.key }}")

                snapshot.children.mapNotNullTo(availableDaysList) { it.key }

                val upcomingDaysList = availableDaysList
                    .filter { it >= todayStr }
                    .sorted()
                    .take(7)

                // Debug: Check the filtered and sorted list
                println("Upcoming Days: $upcomingDaysList")

                _availableDays.value = if (upcomingDaysList.isEmpty()) emptyList() else upcomingDaysList
            }

            override fun onCancelled(error: DatabaseError) {
                // Consider adding logging here to understand why it's being cancelled.
                availableDaysList.clear()
                _availableDays.value = emptyList()
            }
        })
    }

    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun setSelectedPc(pc: Pc) {
        _selectedPc.value = pc
        fetchAvailableDaysForPc(pc) // Now fetches days when a PC is selected
    }
}

