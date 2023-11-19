package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.BuildCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.criminalintent.View.CrimeListFragment
import com.example.criminalintent.View.CriminalDetails

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}