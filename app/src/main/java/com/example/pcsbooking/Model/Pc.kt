package com.example.pcsbooking.Model

data class Pc(
    val id: String = "",
    val reservations: Map<String, TimeSlot> = emptyMap()
)

