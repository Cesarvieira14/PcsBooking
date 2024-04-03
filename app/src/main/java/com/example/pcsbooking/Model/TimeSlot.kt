package com.example.pcsbooking.Model

data class TimeSlot(
    val time: String = "",
    val booked: Boolean = false,
    val bookedBy: String? = null
)
