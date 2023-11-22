package com.example.criminalintent.Model

import com.example.criminalintent.db.CrimeEntity
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class Crime(
    val id: UUID,
    val title: String,
    val date: LocalDate,
    val isSolved: Boolean,
    val requirePolice: Boolean,
    val suspect: String = ""
)

fun Crime.toCrimeEntity(): CrimeEntity =
    CrimeEntity(
        id = this.id,
        title = this.title,
        date = this.date,
        isSolved = this.isSolved,
        suspect = this.suspect
    )