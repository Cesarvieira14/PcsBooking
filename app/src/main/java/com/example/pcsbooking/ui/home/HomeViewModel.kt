package com.example.pcsbooking.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeViewModel : ViewModel() {

    private val _welcomeMessage = MutableLiveData<String>()
    val welcomeMessage: LiveData<String> = _welcomeMessage

    private val _nextBooking = MutableLiveData<Booking?>()
    val nextBooking: LiveData<Booking?> = _nextBooking

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("bookings")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUserData()
        loadNextBooking()
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

    private fun loadNextBooking() {
        val userId = auth.currentUser?.uid ?: return

        database.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var nextBooking: Booking? = null
                    for (bookingSnapshot in dataSnapshot.children) {
                        val booking = bookingSnapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            if (nextBooking == null || booking.date < nextBooking.date) {
                                nextBooking = booking
                            }
                        }
                    }
                    _nextBooking.value = nextBooking
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }
}
