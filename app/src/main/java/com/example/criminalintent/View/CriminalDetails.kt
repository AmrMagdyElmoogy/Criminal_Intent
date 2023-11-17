package com.example.criminalintent.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.ViewModel.CrimeDetailsViewModel
import com.example.criminalintent.ViewModel.CrimeDetailsViewModelFactory
import com.example.criminalintent.databinding.FragmentCriminalDetailsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class CriminalDetails : Fragment() {
    private val args: CriminalDetailsArgs by navArgs()
    private val crimeID by lazy { args.crimeID }
    private val viewModel by viewModels<CrimeDetailsViewModel> {
        CrimeDetailsViewModelFactory(crimeID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    }

    private fun updateUI(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.isEmpty()) {
                crimeTitle.setText(crime.title)
            }
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.updateCrime {
                    it.copy(title = text.toString())
                }
            }
            crimeDate.text = crime.date.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}