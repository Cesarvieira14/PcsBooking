package com.example.pcsbooking.ui.AdminHome

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

data class SummaryData(
    val totalMachines: Int,
    val activeBookings: Int,
    val totalUsers: Int
)

class AdminHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _summaryData = MutableLiveData<SummaryData>()
    val summaryData: LiveData<SummaryData> get() = _summaryData

    private val database = FirebaseDatabase.getInstance().reference
    private val bookingsRef = database.child("bookings")
    private val pcsRef = database.child("pcs")
    private val usersRef = database.child("users")

    init {
        fetchSummaryData()
        observeBookings()
        observeMachines()
        observeUsers()
    }

    private fun fetchSummaryData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalMachines = snapshot.child("pcs").childrenCount.toInt()
                val activeBookings = countActiveBookings(snapshot.child("bookings"))
                val totalUsers = snapshot.child("users").childrenCount.toInt()

                _summaryData.value = SummaryData(totalMachines, activeBookings, totalUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                handleDatabaseError(error)
            }
        })
    }

    private fun observeBookings() {
        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activeBookings = countActiveBookings(snapshot)
                val currentData = _summaryData.value
                if (currentData != null) {
                    _summaryData.value = currentData.copy(activeBookings = activeBookings)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleDatabaseError(error)
            }
        })
    }

    private fun observeMachines() {
        pcsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalMachines = snapshot.childrenCount.toInt()
                val currentData = _summaryData.value
                if (currentData != null) {
                    _summaryData.value = currentData.copy(totalMachines = totalMachines)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleDatabaseError(error)
            }
        })
    }

    private fun observeUsers() {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalUsers = snapshot.childrenCount.toInt()
                val currentData = _summaryData.value
                if (currentData != null) {
                    _summaryData.value = currentData.copy(totalUsers = totalUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleDatabaseError(error)
            }
        })
    }

    private fun countActiveBookings(bookingsSnapshot: DataSnapshot): Int {
        val today = System.currentTimeMillis()
        return bookingsSnapshot.children.count { booking ->
            val date = booking.child("date").getValue(String::class.java)
            val bookingDate =
                date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)?.time }
            bookingDate != null && bookingDate > today
        }
    }

    private fun handleDatabaseError(error: DatabaseError) {
        Log.e("AdminHomeViewModel", "Database error: ${error.message}")
        Toast.makeText(getApplication(), "Error loading data: ${error.message}", Toast.LENGTH_LONG)
            .show()
        _summaryData.value = SummaryData(totalMachines = 0, activeBookings = 0, totalUsers = 0)
    }
}


