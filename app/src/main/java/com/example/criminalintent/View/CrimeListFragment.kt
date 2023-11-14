package com.example.criminalintent.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.ViewModel.CrimeListViewModel
import com.example.criminalintent.databinding.FragmentCrimeListBinding

const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this)[CrimeListViewModel::class.java]
    }

    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: ViewAdapter
    private lateinit var binding: FragmentCrimeListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${crimeListViewModel.crimes.size}")
        crimeListViewModel.crimes.forEach {
            Log.d(TAG, "${it.title} and ${it.requirePolice}")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = binding.crimeRecyclerView
        updateUI(view.context)
    }

    private fun updateUI(context: Context) {
        val crimes = crimeListViewModel.crimes
        adapter = ViewAdapter(crimes, context)
        recycleView.adapter = adapter
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}