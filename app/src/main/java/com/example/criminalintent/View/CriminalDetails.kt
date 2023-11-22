package com.example.criminalintent.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.R
import com.example.criminalintent.ViewModel.CrimeDetailsViewModel
import com.example.criminalintent.ViewModel.CrimeDetailsViewModelFactory
import com.example.criminalintent.canResolveAction
import com.example.criminalintent.databinding.FragmentCriminalDetailsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

private const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
private const val CONTACT_ID = ContactsContract.Contacts._ID
private const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER

class CriminalDetails : Fragment() {
    private val args: CriminalDetailsArgs by navArgs()
    private val crimeID by lazy { args.crimeID }
    private val selectSuspectContact = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) {
        it?.let {
            getSuspectName(it)
        }
    }


    private val viewModel by viewModels<CrimeDetailsViewModel> {
        CrimeDetailsViewModelFactory(crimeID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        requireActivity().onBackPressedDispatcher.addCallback {
            this.isEnabled =
                (binding.crimeTitle.text.isEmpty() || binding.crimeTitle.text.isBlank())
            if (!this.isEnabled) {
                findNavController().popBackStack()
            }
        }
    }

    private var _binding: FragmentCriminalDetailsBinding? = null
    private val binding: FragmentCriminalDetailsBinding
        get() = checkNotNull(_binding) {
            "You cannot use binding now!"
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCriminalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.crime.collect {
                    it?.let { crime ->
                        updateUI(crime)
                    }
                }
            }
        }
        setFragmentResultListener(DatePickerFragment.REQUEST_CODE_1) { _, bundle ->
            val newDate = bundle.getLong(DatePickerFragment.BUNDLE_KEY_1)
            viewModel.updateCrime {
                it.copy(date = LocalDate.ofEpochDay(newDate))
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_CODE) { _, bundle ->
            val newTime: String? = bundle.getString(TimePickerFragment.BUNDLE_KEY)
            binding.crimeTime.text = newTime
        }

    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString =
            if (crime.isSolved) getString(R.string.crime_report_solved) else getString(R.string.crime_report_unsolved)
        val dateString = crime.date.toString()
        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspectText)

    }

    private fun updateUI(crime: Crime) {
        updateTitleOfCrime(crime)
        updateDateOfCrime(crime)
        updateCheckBoxState(crime)
        updateTimeOfCrime(crime)
        updateCrimeSuspectText(crime)
        fireIntentForCrimeReport(crime)
        fireIntentForCrimeSuspect()
        callingSuspect()
    }

    private fun callingSuspect() {
        binding.call.isEnabled = Intent(Intent.ACTION_DIAL).canResolveAction(requireActivity())
        binding.call.setOnClickListener {
            val uri = Uri.parse("tel:${viewModel.suspectPhoneNumber}")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }
    }

    private fun updateCrimeSuspectText(crime: Crime) {
        binding.crimeSuspect.text = crime.suspect.ifEmpty {
            getString(R.string.crime_suspect_text)
        }
    }

    private fun fireIntentForCrimeSuspect() {
        val selectSuspectIntent = selectSuspectContact.contract.createIntent(
            requireContext(),
            null
        )
        binding.crimeSuspect.isEnabled = selectSuspectIntent.canResolveAction(requireActivity())
        binding.crimeSuspect.setOnClickListener {
            selectSuspectContact.launch(null)
        }
    }

    private fun fireIntentForCrimeReport(crime: Crime) {
        binding.crimeReport.setOnClickListener {
            /*val reportIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, crime.suspect)
                putExtra(Intent.EXTRA_SUBJECT, getCrimeReport(crime))
            }
            startActivity(reportIntent)*/
            parseContactSelection(crime)
        }
    }

    private fun updateTimeOfCrime(crime: Crime) {
        binding.crimeTime.setOnClickListener {
            findNavController().navigate(CriminalDetailsDirections.selectTime("13:00PM"))
        }
    }

    private fun updateCheckBoxState(crime: Crime) {
        binding.apply {
            crimeSolved.apply {
                isChecked = crime.isSolved
            }
            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateCrime {
                    it.copy(isSolved = isChecked)
                }
            }
        }
    }

    private fun parseContactSelection(crime: Crime) {
        val queryField = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts._ID
        )

        val queryCursor = requireActivity().contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, queryField, null, null, null
        )
        queryCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val displayNameIndex = cursor.getColumnIndex(DISPLAY_NAME)
                val displayName = cursor.getString(displayNameIndex)
                if (displayName == crime.suspect) {
                    val contactIDIndex =
                        cursor.getColumnIndex(CONTACT_ID)
                    val id = cursor.getString(contactIDIndex)
                    extractPhoneNumberSuspect(id)
                }
            }

        }
    }

    private fun extractPhoneNumberSuspect(id: String?) {
        val queryField = arrayOf(NUMBER)
        val queryCursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            queryField,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(id),
            null
        )
        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val phoneNumberIndex = cursor.getColumnIndex(NUMBER)
                viewModel.updatePhoneNumber(cursor.getString(phoneNumberIndex))
            } else {
                binding.crimeDate.text = " "
            }
        }
    }

    private fun getSuspectName(uri: Uri) {
        val queryField = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver.query(
            uri, queryField, null, null, null
        )

        queryCursor?.use { cursor ->
            if (cursor.moveToNext()) {
                val suspect = cursor.getString(0)
                viewModel.updateCrime {
                    it.copy(suspect = suspect)
                }
            }
        }
    }


    private fun updateDateOfCrime(crime: Crime) {
        binding.apply {
            crimeDate.setOnClickListener {
                it?.let {
                    findNavController().navigate(CriminalDetailsDirections.selectDate(crime.date))
                }
            }
            crimeDate.text = crime.date.toString()
        }
    }

    private fun updateTitleOfCrime(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.isEmpty()) {
                crimeTitle.setText(crime.title)
            }
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.updateCrime {
                    it.copy(title = text.toString())
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
