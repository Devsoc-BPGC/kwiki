package com.jogdand.roomtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Rushikesh Jogdand.
 */
class DataAdapter(private val dataList: MutableList<Data>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_text, parent, false))
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populate(dataList[position])
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val dataTv: TextView = itemView.findViewById(R.id.tv_data)
    fun populate(data: Data) {
        dataTv.text = data.value
    }
}
