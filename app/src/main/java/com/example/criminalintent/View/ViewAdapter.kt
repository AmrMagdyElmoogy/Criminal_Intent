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

class ViewAdapter(
    private val crimes: List<Crime>,
    private val context: Context
) :
    RecyclerView.Adapter<ViewHolder>() {
    /*  override fun getItemViewType(position: Int): Int {
          val crime = crimes[position]
          if (crime.requirePolice)
              return 1
          return 0
      }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> CrimeViewHolderSerious(
                ListItemViewSeriousBinding.inflate(
                    LayoutInflater.from(
                        context
                    ),
                    parent,
                    false
                ).root
            )

            else -> CrimeViewHolder(
                ListItemViewBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ).root
            )
        }
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crime = crimes[position]
        (holder as CrimeViewHolder).bind(crime)
    }
}

class CrimeViewHolder(view: View) : ViewHolder(view), HolderInterface {
    private val binding = ListItemViewBinding.bind(view)
    override val titleTextView: TextView
        get() = binding.CrimeTitle
    override val dateTextView: TextView
        get() = binding.CrimeDate

    init {
        view.setOnClickListener {
            Toast.makeText(
                it.context,
                "${titleTextView.text} has happened at ${dateTextView.text}",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun bind(crime: Crime) {
        titleTextView.text = crime.title
        dateTextView.text = crime.date
        if (!crime.requirePolice)
            binding.CrimeSolved.visibility = View.GONE
        else
            binding.CrimeSolved.visibility = View.VISIBLE
    }
}

class CrimeViewHolderSerious(view: View) : ViewHolder(view), HolderInterface {

    private val binding = ListItemViewSeriousBinding.bind(view)

    override val titleTextView: TextView
        get() = binding.CrimeTextSerious
    override val dateTextView: TextView
        get() = binding.CrimeDateSerious

    init {
        view.setOnClickListener {
            Toast.makeText(
                it.context,
                "${titleTextView.text} has happened at ${dateTextView.text}",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun bind(crime: Crime) {
        titleTextView.text = crime.title
        dateTextView.text = crime.date
    }


}
