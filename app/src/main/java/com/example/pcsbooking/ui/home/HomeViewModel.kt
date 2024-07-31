package com.example.pcsbooking.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _welcomeMessage = MutableLiveData<String>()
    val welcomeMessage: LiveData<String> = _welcomeMessage

    private val _nextBooking = MutableLiveData<Booking?>()
    val nextBooking: LiveData<Booking?> = _nextBooking

    private val _upcomingBookings = MutableLiveData<List<Booking>>()
    val upcomingBookings: LiveData<List<Booking>> = _upcomingBookings

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("bookings")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUserData()
        loadBookings()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userName = dataSnapshot.child("fullName").getValue(String::class.java)
                    _welcomeMessage.value = "Welcome back, $userName!"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun loadBookings() {
        val userId = auth.currentUser?.uid ?: return

        database.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bookings = mutableListOf<Booking>()
                    val currentDate = Date()

                    for (bookingSnapshot in dataSnapshot.children) {
                        val booking = bookingSnapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            val bookingDate = parseBookingDateTime(booking.date, booking.startTime)
                            if (bookingDate != null && bookingDate.after(currentDate)) {
                                bookings.add(booking)
                            }
                        }
                    }
                    // Sort bookings by date and time
                    bookings.sortWith(compareBy({ it.date }, { it.startTime }))
                    // Set the next booking and upcoming bookings
                    _nextBooking.value = bookings.firstOrNull()
                    _upcomingBookings.value =
                        if (bookings.size > 1) bookings.drop(1) else emptyList()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun parseBookingDateTime(date: String, startTime: Int): Date? {
        return try {
            val dateTime = "$date ${startTime / 60}:00"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            dateFormat.parse(dateTime)
        } catch (e: Exception) {
            null
        }
    }
}

