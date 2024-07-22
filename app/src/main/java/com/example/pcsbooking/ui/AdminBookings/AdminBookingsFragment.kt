package com.example.pcsbooking.ui.AdminBookings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pcsbooking.R

class AdminBookingsFragment : Fragment() {

    companion object {
        fun newInstance() = AdminBookingsFragment()
    }

    private lateinit var viewModel: AdminBookingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_bookings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminBookingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}