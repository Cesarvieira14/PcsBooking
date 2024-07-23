package com.example.pcsbooking.ui.AdminUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminUsersViewModel : ViewModel() {
    private val _users = MutableLiveData<List<Pair<String, User>>>()
    val users: LiveData<List<Pair<String, User>>> = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersList = mutableListOf<Pair<String, User>>()
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key ?: continue
                    val user = userSnapshot.getValue(User::class.java) ?: continue
                    usersList.add(Pair(userId, user))
                }
                _users.value = usersList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
