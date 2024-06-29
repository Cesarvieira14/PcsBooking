package com.example.pcsbooking.Model

data class User(
    val email: String,
    val fullName: String,
    val phoneNo: String
) {
    // Construtor padrão vazio necessário para Firebase
    constructor() : this("", "", "")
}
