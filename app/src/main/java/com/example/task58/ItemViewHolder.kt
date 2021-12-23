package com.example.task58

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.database.User
import java.lang.ref.WeakReference

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view = WeakReference(itemView)
    lateinit var textView: TextView
    lateinit var textViewDelete: TextView

    private var id = itemView.findViewById<TextView>(R.id.tvId)
    private var name = itemView.findViewById<TextView>(R.id.tvName)
    private var email = itemView.findViewById<TextView>(R.id.tvEmail)
    private var registrationDate = itemView.findViewById<TextView>(R.id.tvRegDate)
    private var lastLogin = itemView.findViewById<TextView>(R.id.tvLastLogin)
    private var status = itemView.findViewById<TextView>(R.id.tvStatus)

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

    fun bindView(user: User){
        id.text = user.id.toString()
        name.text = user.name
        email.text = user.email
        registrationDate.text = user.registrationDate
        lastLogin.text = user.lastLogin
        status.text = user.status.toString()
    }

    fun updateView() {
        view.get()?.scrollTo(0, 0)
        textView.text = "index $index"
    }
}