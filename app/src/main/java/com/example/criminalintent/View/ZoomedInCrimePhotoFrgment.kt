package com.example.criminalintent.View

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.databinding.ZoomedInCrimePhotoBinding
import com.example.criminalintent.utils.getUriFromFile
import com.example.criminalintent.utils.rotate
import java.io.File

class ZoomedInCrimePhotoFragment : Fragment() {

    private var _binding: ZoomedInCrimePhotoBinding? = null

    private val args: ZoomedInCrimePhotoFragmentArgs by navArgs()

    private val bitmapImage by lazy {
        args.bitmapImage
    }

    private val crimeReport by lazy {
        args.crimeSuspect
    }

    private val binding: ZoomedInCrimePhotoBinding
        get() = checkNotNull(_binding) {
            "You cannot access binding as it is nullable"
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ZoomedInCrimePhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.zoomedIn.setImageBitmap(bitmapImage)
        binding.zoomedIn.rotation = 90f
        binding.zoomedInReport.text = crimeReport
    }
}