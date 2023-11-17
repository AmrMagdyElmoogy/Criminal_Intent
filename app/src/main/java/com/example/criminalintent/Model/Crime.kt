package com.example.criminalintent.Model

import com.example.criminalintent.db.CrimeEntity
import java.util.Date
import java.util.UUID

data class Crime(
    val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val requirePolice: Boolean
)

fun Crime.toCrimeEntity(): CrimeEntity =
    CrimeEntity(id = this.id, title = this.title, date = this.date, isSolved = this.isSolved)