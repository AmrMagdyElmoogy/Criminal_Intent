package com.example.criminalintent.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.criminalintent.ContactProviderInterface
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.Repository.CrimeRepository
import com.example.criminalintent.db.toUiCrime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailsViewModel(
    private val crimeID: UUID
) : ViewModel() {

    private val repo = CrimeRepository.get()

    var suspectPhoneNumber: StringBuffer = StringBuffer("")
        private set

    fun updatePhoneNumber(number: String) {
        suspectPhoneNumber.removeRange(0, suspectPhoneNumber.length)
        suspectPhoneNumber.append("+20 $number")
    }


    // This crime object is for all operations here
    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)


    //This crime object is for UI only, this enforces Unidirectional design arch
    val crime: StateFlow<Crime?>
        get() = _crime.asStateFlow()

    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let(onUpdate)
        }
    }

    init {
        viewModelScope.launch {
            _crime.value = repo.getCrime(crimeID).toUiCrime()
        }
    }

    override fun onCleared() {
        super.onCleared()
        _crime.value?.let {
            repo.update(it)
        }


    }
}

class CrimeDetailsViewModelFactory(private val crimeID: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CrimeDetailsViewModel(crimeID) as T
    }
}