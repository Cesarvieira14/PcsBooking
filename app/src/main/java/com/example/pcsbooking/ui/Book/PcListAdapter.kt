package com.example.pcsbooking.ui.PcList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pcsbooking.Model.Pc
import com.example.pcsbooking.R
import com.google.android.material.chip.Chip

class PcListAdapter(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val PcsList: List<Pc>,
) : ArrayAdapter<Pc>(context, R.layout.item_pc, PcsList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = layoutInflater.inflate(R.layout.item_pc, null, true)

        val currentPc = PcsList.get(position)

        val titleText = rowView.findViewById(R.id.tv_pc_name) as TextView

        titleText.text = currentPc.id

        return rowView
    }

}
