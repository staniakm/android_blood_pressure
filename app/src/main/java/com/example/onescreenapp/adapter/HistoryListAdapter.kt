package com.example.onescreenapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.databinding.LayoutHistoryItemBinding

class HistoryListAdapter :
    RecyclerView.Adapter<HistoryViewHolder>() {

    private var history: List<Pressure> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            LayoutHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        with(holder) {
            with(history[position]) {
                binding.dateLbl.text = this.getDateString()
                binding.lowerLbl.text = this.lowerPressure.toString()
                binding.upperLbl.text = this.upperPressure.toString()
                binding.pulseLbl.text = this.pulse.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun submitList(pressureHistory: List<Pressure>) {
        history = pressureHistory
    }


}


class HistoryViewHolder(val binding: LayoutHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root)
