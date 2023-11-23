package com.example.criminalintent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CrimeEntity::class], version = 4)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun dao(): CrimeDao

}
val migration2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE CrimeEntity ADD COLUMN suspect TEXT NOT NULL DEFAULT ''")
    }
}
val migration3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE CrimeEntity ADD COLUMN photoFileName TEXT")
    }
}
