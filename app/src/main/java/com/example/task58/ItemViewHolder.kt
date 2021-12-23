package com.example.task58

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view = WeakReference(itemView)
    lateinit var textView: TextView
    lateinit var textViewDelete: TextView

    var index = 0
    var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    init {
        view.get()?.let {

            it.setOnClickListener {
                if (view.get()?.scrollX != 0) {
                    view.get()?.scrollTo(0, 0)
                }

            }

            textView = it.findViewById(R.id.tvName)
            textViewDelete = it.findViewById(R.id.textViewDelete)

            textViewDelete.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this)
                }
            }
        }
    }

    fun updateView() {
        view.get()?.scrollTo(0, 0)
        textView.text = "index $index"
    }
}