package com.example.pcsbooking.ui.AdminProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _phoneNo = MutableLiveData<String>()
    val phoneNo: LiveData<String> get() = _phoneNo

    fun getAdminProfile() {
        val userId = auth.currentUser?.uid ?: return

        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _fullName.value = snapshot.child("fullName").getValue(String::class.java)
                _email.value = snapshot.child("email").getValue(String::class.java)
                _phoneNo.value = snapshot.child("phoneNo").getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun resetPassword() {
        val userEmail = auth.currentUser?.email ?: return
        auth.sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Notify user that the email has been sent
                } else {
                    // Handle error
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}

