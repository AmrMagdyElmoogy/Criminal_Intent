package com.example.criminalintent.View

import android.widget.TextView
import com.example.criminalintent.Model.Crime

interface HolderInterface {
    val titleTextView: TextView
    val dateTextView: TextView
    fun bind(crime: Crime)
}