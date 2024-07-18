package com.example.pcsbooking.ui.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Booking
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ManageViewModel : ViewModel() {

    private val _futureBookings = MutableLiveData<List<Booking>>()
    val futureBookings: LiveData<List<Booking>> = _futureBookings

    private val _pastBookings = MutableLiveData<List<Booking>>()
    val pastBookings: LiveData<List<Booking>> = _pastBookings

    private val bookingIdMap = mutableMapOf<Booking, String>()

    fun fetchUserBookings() {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("bookings")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val futureBookingsList = mutableListOf<Booking>()
                val pastBookingsList = mutableListOf<Booking>()
                bookingIdMap.clear()

                val currentDate = Date()
                for (bookingSnapshot in snapshot.children) {
                    val booking = bookingSnapshot.getValue(Booking::class.java)
                    if (booking?.userId == userId) {
                        val bookingDate = parseDate(booking.date)
                        if (bookingDate.after(currentDate)) {
                            futureBookingsList.add(booking)
                        } else {
                            pastBookingsList.add(booking)
                        }
                        bookingIdMap[booking] = bookingSnapshot.key ?: ""
                    }
                }
                _futureBookings.value = futureBookingsList
                _pastBookings.value = pastBookingsList
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
                _futureBookings.value = _futureBookings.value?.filter { it != booking }
            } else {
                // Handle the failure
            }
        }
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }
}
