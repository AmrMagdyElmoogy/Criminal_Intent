package com.example.criminalintent.ViewModel

import androidx.lifecycle.ViewModel
import com.example.criminalintent.Model.Crime
import java.util.Date
import java.util.UUID
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.criminalintent.Repository.CrimeRepository
import com.example.criminalintent.db.CrimeEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {
    val repo = CrimeRepository.get()
    private var _crimes = MutableStateFlow<List<Crime>>(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()


    init {
        viewModelScope.launch {
            repo.getCrimes().collect {
                _crimes.value = it
            }
        }
    }
}