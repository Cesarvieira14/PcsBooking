package com.example.pcsbooking.ui.Book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.Model.PcRepository

class BookViewModel : ViewModel() {
    private val repository = PcRepository()
    val pcs = repository.pcs

    // Add this line
    val selectedPc = MutableLiveData<Pc>()

    fun bookPc(pcId: String, timeSlot: String) {
        repository.bookPc(pcId, timeSlot)
    }
}
