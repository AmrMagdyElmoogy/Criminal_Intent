package com.example.criminalintent.ViewModel

import androidx.lifecycle.ViewModel
import com.example.criminalintent.Model.Crime
import java.util.Date
import java.util.UUID
import android.text.format.DateFormat

class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0..30) {
            crimes.add(
                Crime(
                    id = UUID.randomUUID(),
                    title = "Crime $i",
                    isSolved = i % 2 == 0,
                    date = DateFormat.format("yyyy-MM-dd", Date().time)
                        .toString(),
                    requirePolice = i % 5 == 0
                )
            )
        }
    }
}