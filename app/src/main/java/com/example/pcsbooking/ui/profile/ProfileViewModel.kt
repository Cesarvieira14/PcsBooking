package com.example.pcsbooking.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private var database = Firebase.database.reference
    private val _fullName = MutableLiveData<String>()
    private val _phoneNo = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()


    val fullName: LiveData<String> = _fullName
    val phoneNo: LiveData<String> = _phoneNo
    val email: LiveData<String> = _email

    init {
        loadUserDetails()
    }

    private fun loadUserDetails() {
        val user = auth.currentUser
        _email.value = user?.email // Fetch email directly from FirebaseAuth

        val userId = user?.uid
        if (userId != null) {
            database.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
                _fullName.value = dataSnapshot.child("fullName").value.toString()
                _phoneNo.value = dataSnapshot.child("phoneNo").value.toString()
            }.addOnFailureListener {
                _fullName.value = "Failed to load"
                _phoneNo.value = "Failed to load"
            }
        }
    }
    private val _resetPasswordResult = MutableLiveData<Boolean>()
    val resetPasswordResult: LiveData<Boolean> = _resetPasswordResult

    fun resetPassword(email: String, callback: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                // Password reset email sent successfully
                callback(true)
            }
            .addOnFailureListener { e ->
                // Error occurred while sending password reset email
                callback(false)
            }
    }

    fun logout() {
        auth.signOut()
    }
}
