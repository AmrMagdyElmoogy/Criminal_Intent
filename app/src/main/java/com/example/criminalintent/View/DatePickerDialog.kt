package com.example.criminalintent.View

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    private val calender = Calendar.getInstance()
    private val args: DatePickerFragmentArgs by navArgs()
    private var initialYear = calender.get(Calendar.YEAR)
    private var initialMonth = calender.get(Calendar.MONTH)
    private var initialDay = calender.get(Calendar.DAY_OF_WEEK)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calender.apply {
            initialYear = args.crimeDate.year
            initialMonth = args.crimeDate.month.value
            initialDay = args.crimeDate.dayOfWeek.value
        }

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val resultCode = LocalDate.of(year, month+1, dayOfMonth).toEpochDay()
            setFragmentResult(
                requestKey = REQUEST_CODE_1, bundleOf(
                    BUNDLE_KEY_1 to resultCode
                )
            )
        }
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }


    companion object {
        const val REQUEST_CODE_1 = "REQUEST_CODE_DATE"
        const val BUNDLE_KEY_1 = "BUNDLE_KEY_DATE"
    }
}