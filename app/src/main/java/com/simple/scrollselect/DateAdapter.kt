package com.simple.scrollselect

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DateAdapter(private val dateDataList: ArrayList<LabelerDate>, private val paddingHeightDate: Int) :
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    companion object {
        const val VIEW_TYPE_PADDING = 1
        const val VIEW_TYPE_ITEM = 2
    }

    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerv_item, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerv_item, parent, false)

            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            layoutParams.height = paddingHeightDate
            view.layoutParams = layoutParams
            DateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val labelerDate = dateDataList[position]
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            holder.tvDate.text = labelerDate.number
            holder.tvDate.visibility = View.VISIBLE

            if (position == selectedItem) {
                holder.tvDate.setTextColor(Color.parseColor("#76FF03"))
                holder.tvDate.setTextSize(35f)
            } else {
                holder.tvDate.setTextColor(Color.BLACK)
                holder.tvDate.setTextSize(18f)
            }
        } else {
            holder.tvDate.visibility = View.INVISIBLE
        }
    }

    fun setSelectedItem(selectedItem: Int) {
        this.selectedItem = selectedItem
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dateDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        val labelerDate = dateDataList[position]
        return if (labelerDate.type == VIEW_TYPE_PADDING) {
            VIEW_TYPE_PADDING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_time)
    }
}