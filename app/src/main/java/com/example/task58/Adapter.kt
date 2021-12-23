package com.example.task58

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class Adapter : RecyclerView.Adapter<ItemViewHolder>() {

    var list = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.index = list[position]

        holder.onDeleteClick = {
            removeItem(it)
        }

        holder.updateView()
    }

    override fun getItemCount(): Int = list.size

    fun reload(list: List<Int>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        list.removeAt(position)
        notifyItemRemoved(position)
    }

}