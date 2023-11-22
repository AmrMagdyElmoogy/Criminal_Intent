package com.example.criminalintent.Repository

import android.content.Context
import androidx.room.Room
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.Model.toCrimeEntity
import com.example.criminalintent.db.CrimeDatabase
import com.example.criminalintent.db.CrimeEntity
import com.example.criminalintent.db.migration2_3
import com.example.criminalintent.db.toUiCrime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database = Room.databaseBuilder(context, CrimeDatabase::class.java, DATABASE_NAME)
        .addMigrations(migration2_3)
        .build()

    private val dao = database.dao()

    fun getCrimes(): Flow<List<Crime>> {
        val fetchedCrimes = dao.getCrimes()
        var transformedCrimes: Flow<List<Crime>> = flowOf(emptyList())
        transformedCrimes = fetchedCrimes.transform { list ->
            val newList = list.map { it.toUiCrime() }
            emit(newList)
        }
        return transformedCrimes
    }

    suspend fun getCrime(uuid: UUID) = dao.getCrime(uuid)

    fun update(crime: Crime) {
        GlobalScope.launch {
            dao.updateCrime(crime.toCrimeEntity())
        }
    }

    suspend fun deleteCrime(crime: CrimeEntity) = dao.delete(crime)

    suspend fun insert(crime: CrimeEntity) = dao.insert(crime)

    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}