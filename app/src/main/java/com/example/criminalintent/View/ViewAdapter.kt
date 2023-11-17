package com.example.criminalintent.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.transition.Visibility
import com.example.criminalintent.Model.Crime
import com.example.criminalintent.databinding.ListItemViewBinding
import com.example.criminalintent.databinding.ListItemViewSeriousBinding
import java.util.UUID

class ViewAdapter(
    private val crimes: List<Crime>,
    private val context: Context,
    private val onNavigateToCrimeDetails: (UUID) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder =

        CrimeViewHolder(
            ListItemViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root,
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crime = crimes[position]
        (holder as CrimeViewHolder).bind(crime) { onNavigateToCrimeDetails(crime.id) }
    }

    override fun getItemCount() = crimes.size


}

class CrimeViewHolder(view: View) :
    RecyclerView.ViewHolder(view),
    HolderInterface {
    private val binding = ListItemViewBinding.bind(view)
    override val titleTextView: TextView
        get() = binding.CrimeTitle
    override val dateTextView: TextView
        get() = binding.CrimeDate

    fun bind(crime: Crime, onNavigateToCrimeDetails: (UUID) -> Unit) {

        binding.root.setOnClickListener {
            onNavigateToCrimeDetails(crime.id)
        }

        titleTextView.text = crime.title
        dateTextView.text = crime.date.toString()
        if (!crime.requirePolice)
            binding.CrimeSolved.visibility = View.GONE
        else
            binding.CrimeSolved.visibility = View.VISIBLE
    }
}
