package com.example.pcsbooking.Model

data class Booking(
    val pcId: String,
    val userId: String,
    val date: String,
    val startTime: Int,
    val endTime: Int
) {
    // Construtor padrão vazio necessário para Firebase
    constructor() : this("", "", "", 0, 0)
}
