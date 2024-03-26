package com.example.pcsbooking.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    // Expose the user's email. Assuming email is used for user details.
    val userEmail: String
        get() = auth.currentUser?.email ?: "No User Logged In"

    fun logout() {
        auth.signOut()
    }
}