package com.example.pcsbooking.ui.AdminBookings

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var bookingId: String
    private lateinit var userId: String
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Initialize Firebase Database Reference
        dbRef = FirebaseDatabase.getInstance().reference

        // Retrieve the booking details from the Intent
        bookingId = intent.getStringExtra("BOOKING_ID") ?: ""
        val pcId = intent.getStringExtra("PC_ID") ?: ""
        userId = intent.getStringExtra("USER_ID") ?: ""
        val date = intent.getStringExtra("DATE") ?: ""
        val startTime = intent.getIntExtra("START_TIME", 0)
        val endTime = intent.getIntExtra("END_TIME", 0)

        // Populate the UI with the booking details
        findViewById<TextView>(R.id.tvPcId).text = pcId
        findViewById<TextView>(R.id.tvDate).text = date
        findViewById<TextView>(R.id.tvStartTime).text = convertMinutesToHours(startTime)
        findViewById<TextView>(R.id.tvEndTime).text = convertMinutesToHours(endTime)
        findViewById<TextView>(R.id.tvBookingId).text = bookingId
        findViewById<TextView>(R.id.tvUserId).text = userId

        // Fetch user name from Firebase
        fetchUserName(userId) { userName ->
            findViewById<TextView>(R.id.tvUserFullName).text = userName
        }

        // Setup cancel button functionality
        findViewById<Button>(R.id.btnCancelBooking).setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun convertMinutesToHours(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%02d:%02d", hours, mins)
    }

    private fun fetchUserName(userId: String, callback: (String) -> Unit) {
        if (userId.isNotEmpty()) {
            dbRef.child("users").child(userId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val userName =
                        snapshot.child("fullName").getValue(String::class.java) ?: "Unknown User"
                    Log.d("BookingDetailsActivity", "User Name: $userName")
                    callback(userName)
                } else {
                    Log.d("BookingDetailsActivity", "User not found")
                    callback("User not found")
                }
            }.addOnFailureListener { exception ->
                Log.e("BookingDetailsActivity", "Failed to fetch user name", exception)
                callback("Error fetching user name")
            }
        } else {
            Log.e("BookingDetailsActivity", "User ID is empty")
            callback("User ID is empty")
        }
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Confirm Cancellation")
        dialogBuilder.setMessage("Are you sure you want to cancel this booking?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            cancelBooking()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }

    private fun cancelBooking() {
        if (bookingId.isNotEmpty()) {
            dbRef.child("bookings").child(bookingId).removeValue()
                .addOnSuccessListener {
                    // Booking successfully cancelled
                    finish() // Close the activity
                }
                .addOnFailureListener { exception ->
                    Log.e("BookingDetailsActivity", "Failed to cancel booking", exception)
                    // Handle the error
                    // You might want to show a Toast or Snackbar here
                }
        } else {
            Log.e("BookingDetailsActivity", "Booking ID is empty")
        }
    }
}

