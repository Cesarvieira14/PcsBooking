package com.example.pcsbooking.ui.AdminUsers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pcsbooking.databinding.FragmentAdminUsersBinding
import com.example.pcsbooking.Model.User

class AdminUsersFragment : Fragment() {

    private var _binding: FragmentAdminUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UsersAdapter(emptyList()) { userPair ->
            displayUserDetails(userPair)
        }
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }
    }

    private fun displayUserDetails(userPair: Pair<String, User>) {
        val (userId, user) = userPair
        val userDetails =
            "User ID: $userId\nName: ${user.fullName}\nEmail: ${user.email}\nPhone: ${user.phoneNo}"
        Toast.makeText(requireContext(), userDetails, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

