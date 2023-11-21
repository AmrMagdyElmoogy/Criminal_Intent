package com.example.criminalintent.View

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.R
import com.example.criminalintent.SwipeToDelete
import com.example.criminalintent.ViewModel.CrimeListViewModel
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class CrimeListFragment : Fragment() {

    private val viewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this)[CrimeListViewModel::class.java]
    }
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: ViewAdapter
/*    private lateinit var emptyImage: ImageView
    private lateinit var emptyTextView: TextView*/

    private lateinit var emptyListView: ConstraintLayout
    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding"
        }


    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onCreateOptionsMenu(menu, inflater)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onOptionsItemSelected(item)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                writeNewCrime()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun writeNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false,
                requirePolice = false
            )
            viewModel.insertNewCrime(newCrime)
            findNavController().navigate(
                CrimeListFragmentDirections.actionCrimeListFragmentToCriminalDetails(
                    newCrime.id
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = binding.crimeRecyclerView
        emptyListView = binding.emptyListID.root
        itemTouchHelper = ItemTouchHelper(SwipeToDelete {
            adapter.notifyItemRemoved(it)
            viewModel.deleteCrimeAfterSwapping(it)
        })
        itemTouchHelper.attachToRecyclerView(recycleView)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.crimes.collect {
                it.checkCrimeState()
                updateUI(view.context, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI(context: Context, crimes: List<Crime>) {
        adapter = ViewAdapter(crimes, context) {
            findNavController().navigate(
                CrimeListFragmentDirections.actionCrimeListFragmentToCriminalDetails(
                    it
                )
            )
        }
        recycleView.adapter = adapter
    }

    private fun List<Crime>.checkCrimeState() {
        when {
            isEmpty() -> {
                recycleView.visibility = View.INVISIBLE
                emptyListView.visibility = View.VISIBLE
            }
            else -> {
                recycleView.visibility = View.VISIBLE
                emptyListView.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}

