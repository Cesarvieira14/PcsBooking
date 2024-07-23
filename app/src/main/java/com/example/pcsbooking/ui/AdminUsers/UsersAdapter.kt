package com.example.pcsbooking.ui.AdminUsers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.User
import com.example.pcsbooking.R

class UsersAdapter(
    private var users: List<Pair<String, User>>,
    private val onUserClick: (Pair<String, User>) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private var filteredUsers: List<Pair<String, User>> = users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userPair = filteredUsers[position]
        holder.bind(userPair, onUserClick)
    }

    override fun getItemCount(): Int = filteredUsers.size

    fun updateUsers(newUsers: List<Pair<String, User>>) {
        users = newUsers
        filter("")
    }

    fun filter(query: String) {
        filteredUsers = if (query.isEmpty()) {
            users
        } else {
            users.filter { (_, user) ->
                user.fullName.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userIdTextView: TextView = itemView.findViewById(R.id.userIdTextView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userTypeTextView: TextView = itemView.findViewById(R.id.userTypeTextView)

        fun bind(userPair: Pair<String, User>, onUserClick: (Pair<String, User>) -> Unit) {
            val (userId, user) = userPair
            userIdTextView.text = userId
            userNameTextView.text = user.fullName
            userTypeTextView.text = if (user.admin) "Admin" else "User"
            itemView.setOnClickListener { onUserClick(userPair) }
        }
    }
}