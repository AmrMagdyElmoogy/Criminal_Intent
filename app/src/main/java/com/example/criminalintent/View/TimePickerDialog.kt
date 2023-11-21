package com.example.criminalintent.View

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment() {

    private val calender = Calendar.getInstance()
    private val initialHour = calender.get(Calendar.HOUR_OF_DAY)
    private val initialMinute = calender.get(Calendar.MINUTE)
    private val args: TimePickerFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val timePicker = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val formatter = DateTimeFormatter.ofPattern("hh:mm")
            val localTime = LocalTime.of(hourOfDay, minute)
            val time = formatter.format(localTime)
            setFragmentResult(REQUEST_CODE, bundleOf(BUNDLE_KEY to time))
        }

        return TimePickerDialog(
            requireContext(),
            timePicker,
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