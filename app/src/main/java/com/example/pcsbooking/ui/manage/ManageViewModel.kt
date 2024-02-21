package com.example.pcsbooking.ui.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Booking Fragment"
    }
    val text: LiveData<String> = _text
}