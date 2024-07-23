package com.example.pcsbooking.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pcsbooking.databinding.FragmentProfileBinding
import com.example.pcsbooking.ui.authentication.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserDetails()

        viewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            binding.fullNameTextView.text = fullName
        }

        viewModel.email.observe(viewLifecycleOwner) { email ->
            binding.emailTextView.text = email
        }

        viewModel.phoneNo.observe(viewLifecycleOwner) { phoneNo ->
            binding.phoneTextView.text = phoneNo
        }

        binding.resetPasswordButton.setOnClickListener {
            val email = viewModel.email.value
            if (!email.isNullOrBlank()) {
                viewModel.resetPassword(email) { success ->
                    val message = if (success) {
                        "Password reset email sent successfully"
                    } else {
                        "Failed to send password reset email"
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


