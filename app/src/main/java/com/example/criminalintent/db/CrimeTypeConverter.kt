package com.example.criminalintent.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.Date
import java.util.UUID

class CrimeTypeConverters {

    @TypeConverter
    fun fromUUID(id: UUID): String {
        return id.toString()
    }

    @TypeConverter
    fun toUUID(str: String): UUID {
        return UUID.fromString(str)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): LocalDate {
        return LocalDate.ofEpochDay(millisSinceEpoch)
    }
}