package com.example.pcsbooking.ui.AdminProfile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AdminProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun logout() {
        auth.signOut()
    }
}