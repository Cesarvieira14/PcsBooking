package com.example.pcsbooking.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pcsbooking.databinding.FragmentProfileBinding
import com.example.pcsbooking.ui.authentication.LoginActivity
import com.example.pcsbooking.ui.profile.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe all user details
        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            profileViewModel.fullName.observe(viewLifecycleOwner) { fullName ->
                profileViewModel.phoneNo.observe(viewLifecycleOwner) { phoneNo ->
                    val userDetailsText = buildString {
                        append("$email\n")
                        append("Name: $fullName\n")
                        append("Phone: $phoneNo")
                    }
                    binding.textProfile.text = userDetailsText
                }
            }
        }
        // Set click listener for logout button
        binding.LogoutButton.setOnClickListener {
            profileViewModel.logout()
            navigateToLogin()
        }

        // Set click listener for reset password button
        binding.resetPasswordButton.setOnClickListener {
            val email = profileViewModel.email.value
            if (!email.isNullOrBlank()) {
                profileViewModel.resetPassword(email) { success ->
                    val message = if (success) {
                        "Password reset email sent successfully"
                    } else {
                        "Failed to send password reset email"
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
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
