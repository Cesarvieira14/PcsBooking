package com.example.pcsbooking.ui.Book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.R

class PcAdapter(private var pcs: List<Pc>, private val onPcClick: (Pc) -> Unit) : RecyclerView.Adapter<PcAdapter.PcViewHolder>() {

    class PcViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPcName: TextView = view.findViewById(R.id.tv_pc_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pc, parent, false)
        return PcViewHolder(view)
    }

    override fun onBindViewHolder(holder: PcViewHolder, position: Int) {
        val pc = pcs[position]
        holder.tvPcName.text = pc.id
        holder.itemView.setOnClickListener { onPcClick(pc) }
    }

    override fun getItemCount() = pcs.size

    fun updatePcs(pcs: List<Pc>) {
        this.pcs = pcs
        notifyDataSetChanged()
    }
}
