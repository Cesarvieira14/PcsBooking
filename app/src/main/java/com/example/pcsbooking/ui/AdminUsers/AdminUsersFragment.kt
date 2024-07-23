package com.example.pcsbooking.ui.AdminUsers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
            val (userId, user) = userPair
            val intent = Intent(requireContext(), UserDetailsActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", user.fullName)
                putExtra("USER_EMAIL", user.email)
                putExtra("USER_PHONE", user.phoneNo)
                putExtra("USER_IS_ADMIN", user.admin)
            }
            startActivity(intent)
        }

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }

        binding.searchBar.addTextChangedListener { text ->
            adapter.filter(text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
