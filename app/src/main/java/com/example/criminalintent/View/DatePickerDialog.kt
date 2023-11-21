package com.example.criminalintent.View

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    private val calender = Calendar.getInstance()
    private val args: DatePickerFragmentArgs by navArgs()
    private val initialYear = calender.get(Calendar.YEAR)
    private val initialMonth = calender.get(Calendar.MONTH)
    private val initialDay = calender.get(Calendar.DAY_OF_WEEK)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calender.time = args.crimeDate
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val resultCode = GregorianCalendar(year, month, dayOfMonth).time
            setFragmentResult(
                requestKey = REQUEST_CODE_1, bundleOf(
                    BUNDLE_KEY_1 to resultCode.time
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