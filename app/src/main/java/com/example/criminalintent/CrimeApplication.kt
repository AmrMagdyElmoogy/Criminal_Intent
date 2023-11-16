package com.example.criminalintent

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import com.example.criminalintent.Repository.CrimeRepository

class CrimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }

}