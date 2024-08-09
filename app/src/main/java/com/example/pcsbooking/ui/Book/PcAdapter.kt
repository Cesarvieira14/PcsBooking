package com.example.pcsbooking.ui.Book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.R

// PcAdapter.kt
class PcAdapter(
    private val context: Context,
    private var pcsList: List<Pc>,
    private val onItemClick: (Pc) -> Unit
) : RecyclerView.Adapter<PcAdapter.PcViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pc, parent, false)
        return PcViewHolder(view)
    }

    override fun onBindViewHolder(holder: PcViewHolder, position: Int) {
        val pc = pcsList[position]
        holder.bind(pc)
        holder.itemView.setOnClickListener {
            onItemClick(pc)
        }
    }

    override fun getItemCount(): Int {
        return pcsList.size
    }

    fun submitList(newList: List<Pc>) {
        pcsList = newList
        notifyDataSetChanged()
    }

    inner class PcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.tv_pc_name)
        private val descriptionText: TextView = itemView.findViewById(R.id.tv_pc_description)
        private val locationText: TextView = itemView.findViewById(R.id.tv_pc_location)

        fun bind(pc: Pc) {
            nameText.text = pc.name
            descriptionText.text = pc.description
            locationText.text = pc.location
        }
    }
}

