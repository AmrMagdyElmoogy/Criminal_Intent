package com.example.criminalintent.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.criminalintent.Model.Crime
import java.time.LocalDate
import java.util.Date
import java.util.UUID

@Entity
data class CrimeEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "isSolved")
    val isSolved: Boolean,
    @ColumnInfo(name = "suspect")
    val suspect: String
) {
}

fun CrimeEntity.toUiCrime(): Crime {
    return Crime(
        id = id,
        title = title,
        date = date,
        isSolved = isSolved,
        requirePolice = false,
        suspect = suspect
    )
}