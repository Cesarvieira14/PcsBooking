package com.example.pcsbooking.ui.AdminProfile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.pcsbooking.R
import com.example.pcsbooking.databinding.FragmentAdminProfileBinding
import com.example.pcsbooking.ui.authentication.LoginActivity

class AdminProfileFragment : Fragment() {

    private var _binding: FragmentAdminProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdminProfile()

        viewModel.fullName.observe(viewLifecycleOwner, Observer { fullName ->
            binding.fullNameTextView.text = fullName
        })

        viewModel.email.observe(viewLifecycleOwner, Observer { email ->
            binding.emailTextView.text = email
        })

        viewModel.phoneNo.observe(viewLifecycleOwner, Observer { phoneNo ->
            binding.phoneTextView.text = phoneNo
        })

        binding.resetPasswordButton.setOnClickListener {
            viewModel.resetPassword()
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val loginIntent = Intent(requireContext(), LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        requireActivity().finish() // Finish the current activity to prevent returning to it
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

