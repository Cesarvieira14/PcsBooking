package com.example.pcsbooking.ui.AdminBookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.example.pcsbooking.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AdminBookingsViewModel : ViewModel() {

    private val _bookings = MutableLiveData<List<Pair<String, Booking>>>()
    val bookings: LiveData<List<Pair<String, Booking>>> = _bookings

    private val _users = MutableLiveData<Map<String, User>>()
    val users: LiveData<Map<String, User>> = _users

    init {
        fetchBookings()
        fetchUsers()
    }

    private fun fetchBookings() {
        val database = FirebaseDatabase.getInstance().getReference("bookings")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookingsList = mutableListOf<Pair<String, Booking>>()
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                for (bookingSnapshot in snapshot.children) {
                    val bookingId = bookingSnapshot.key ?: continue
                    val booking = bookingSnapshot.getValue(Booking::class.java) ?: continue

                    // Filter bookings based on the date
                    if (booking.date >= currentDate) {
                        bookingsList.add(Pair(bookingId, booking))
                    }
                }
                _bookings.value = bookingsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun fetchUsers() {
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersMap = mutableMapOf<String, User>()
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key ?: continue
                    val user = userSnapshot.getValue(User::class.java) ?: continue
                    usersMap[userId] = user
                }
                _users.value = usersMap
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun getUserName(userId: String): String? {
        return _users.value?.get(userId)?.fullName
    }

    fun cancelBooking(bookingId: String) {
        val database = FirebaseDatabase.getInstance().getReference("bookings").child(bookingId)
        database.removeValue()
    }
}
