package com.example.criminalintent.View

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment() {

    private val calender = Calendar.getInstance()
    private val initialHour = calender.get(Calendar.HOUR_OF_DAY)
    private val initialMinute = calender.get(Calendar.MINUTE)
    private val args: TimePickerFragmentArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

//        val time = args.crimeTime

        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

            val time = "$hourOfDay:$minute"

            setFragmentResult(REQUEST_CODE, bundleOf(BUNDLE_KEY to time))
        }

        return TimePickerDialog(
            requireContext(),
            null,
            initialHour,
            initialMinute,
            false
        )
    }

    companion object {
        const val REQUEST_CODE = "REQUEST_CODE"
        const val BUNDLE_KEY = "TIME_BUNDLE_KEY"

    }
}