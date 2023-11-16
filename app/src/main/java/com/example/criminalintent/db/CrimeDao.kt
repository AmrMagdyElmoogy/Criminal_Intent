package com.example.criminalintent.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.criminalintent.Model.Crime
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CrimeDao {
    @Query("SELECT * FROM CrimeEntity")
    fun getCrimes(): Flow<List<CrimeEntity>>

    @Query("SELECT * FROM CrimeEntity WHERE id=:id")
    suspend fun getCrime(id: UUID): CrimeEntity

    @Insert(entity = CrimeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crime: CrimeEntity): Long
}