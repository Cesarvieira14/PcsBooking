package com.example.pcsbooking.Model

data class Pc(
    var id: String,
    var name: String,
    var description: String,
    var location: String
) {
    constructor() : this("", "", "", "")
}
