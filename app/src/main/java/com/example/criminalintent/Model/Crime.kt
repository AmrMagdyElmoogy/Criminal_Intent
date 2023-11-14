package com.example.criminalintent.Model

import java.util.UUID

data class Crime(
    val id: UUID,
    val title: String,
    val date: String,
    val isSolved: Boolean,
    val requirePolice : Boolean
)
