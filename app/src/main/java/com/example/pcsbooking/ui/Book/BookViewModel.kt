package com.example.pcsbooking.ui.Book

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.Model.Pc
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookingsRef = FirebaseDatabase.getInstance().getReference("bookings")
    private val pcsRef = FirebaseDatabase.getInstance().getReference("pcs")

    private val _pcs = MutableLiveData<List<Pc>>()
    val pcs: LiveData<List<Pc>> get() = _pcs

    private val _availableDays = MutableLiveData<List<String>>()
    val availableDays: LiveData<List<String>> get() = _availableDays

    private val _availableTimeSlots = MutableLiveData<List<String>>()
    val availableTimeSlots: LiveData<List<String>> get() = _availableTimeSlots

    // Selected PC and day LiveData
    private val _selectedPc = MutableLiveData<Pc>()
    val selectedPc: LiveData<Pc> get() = _selectedPc

    private val _selectedDay = MutableLiveData<String>()
    val selectedDay: LiveData<String> get() = _selectedDay

    init {
        fetchPcs()
    }

    fun fetchPcs() {
        pcsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pcsList = mutableListOf<Pc>()
                for (pcSnapshot in snapshot.children) {
                    val pc = pcSnapshot.getValue(Pc::class.java)
                    pcSnapshot.key?.let {
                        if (pc != null) {
                            pc.id = it
                        }
                    }

                    pc?.let {
                        pcsList.add(pc)
                    }
                }
                _pcs.value = pcsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchAvailableDays() {
        val daysInRange = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        for (i in 0 until 7) {
            val date = calendar.time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            daysInRange.add(dateFormat.format(date))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        _availableDays.value = daysInRange
    }

    fun fetchAvailableTimeSlots(selectedDate: String, pcId: String) {
        val startTime = 8 * 60 // 8 AM in minutes
        val endTime = 20 * 60 // 8 PM in minutes

        val availableSlots = mutableListOf<String>()

        for (time in startTime until endTime step (2 * 60)) { // 2 hours slots
            availableSlots.add("${time / 60}:00 - ${(time + 120) / 60}:00")
        }

        // Now filter out booked time slots
        bookingsRef.orderByChild("date").equalTo(selectedDate)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookedSlots = mutableListOf<String>()

                    snapshot.children.forEach { bookingSnapshot ->
                        val startTime =
                            bookingSnapshot.child("startTime").getValue(Int::class.java) ?: return
                        val endTime =
                            bookingSnapshot.child("endTime").getValue(Int::class.java) ?: return

                        for (time in startTime until endTime step (2 * 60)) {
                            val slot = "${time / 60}:00 - ${(time + 120) / 60}:00"
                            bookedSlots.add(slot)
                        }
                    }

                    availableSlots.removeAll(bookedSlots)
                    _availableTimeSlots.value = availableSlots
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancellation
                    _availableTimeSlots.value = emptyList()
                }
            })
    }

    fun setSelectedPc(pc: Pc) {
        _selectedPc.value = pc
    }

    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun bookPc(pcId: String, userId: String, date: String, startTime: Int, endTime: Int) {
        val bookingId = bookingsRef.push().key ?: return
        val booking = Booking(pcId, userId, date, startTime, endTime)

        bookingsRef.child(bookingId).setValue(booking)
            .addOnSuccessListener {
                // Fetch time slots to update list
                fetchAvailableTimeSlots(selectedDay.value.toString(), selectedPc.value!!.id)
                Toast.makeText(getApplication(), "Booking successful.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Handle failure if needed
                Toast.makeText(getApplication(), "Something went wrong on your booking.", Toast.LENGTH_SHORT).show()
            }
    }
}





