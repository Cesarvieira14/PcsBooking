package com.example.pcsbooking.ui.AdminUsers

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pcsbooking.R

class AdminUsersFragment : Fragment() {

    companion object {
        fun newInstance() = AdminUsersFragment()
    }

    private lateinit var viewModel: AdminUsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_users, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminUsersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}