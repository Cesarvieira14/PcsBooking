package com.example.pcsbooking.ui.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ManageViewModel : ViewModel() {

    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> = _bookings

    private val bookingIdMap = mutableMapOf<Booking, String>()

    fun fetchUserBookings() {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("bookings")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userBookings = mutableListOf<Booking>()
                bookingIdMap.clear()
                for (bookingSnapshot in snapshot.children) {
                    val booking = bookingSnapshot.getValue(Booking::class.java)
                    if (booking?.userId == userId) {
                        userBookings.add(booking)
                        bookingIdMap[booking] = bookingSnapshot.key ?: ""
                    }
                }
                _bookings.value = userBookings
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun unbook(booking: Booking) {
        val bookingId = bookingIdMap[booking] ?: return
        val database = FirebaseDatabase.getInstance().getReference("bookings").child(bookingId)
        database.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Remove the booking from the list and notify observers
                _bookings.value = _bookings.value?.filter { it != booking }
            } else {
                // Handle the failure
            }
        }
    }
}
