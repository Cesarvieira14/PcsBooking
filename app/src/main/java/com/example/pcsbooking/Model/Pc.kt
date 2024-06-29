package com.example.pcsbooking.Model

data class Pc(
    val id: String,
    val name: String,
    val description: String,
    val location: String
) {
    // Construtor padrão vazio necessário para Firebase
    constructor() : this("", "", "", "")
}
