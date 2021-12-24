package com.example.task58

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.Models.User
import java.lang.ref.WeakReference

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view = WeakReference(itemView)
    lateinit var textView: TextView
    var textViewDelete = itemView.findViewById<TextView>(R.id.textViewDelete)
    var textViewBlock = itemView.findViewById<TextView>(R.id.textViewBlock)

    private var id = itemView.findViewById<TextView>(R.id.tvId)
    private var name = itemView.findViewById<TextView>(R.id.tvName)
    private var email = itemView.findViewById<TextView>(R.id.tvEmail)
    private var registrationDate = itemView.findViewById<TextView>(R.id.tvRegDate)
    private var lastLogin = itemView.findViewById<TextView>(R.id.tvLastLogin)
    private var status = itemView.findViewById<TextView>(R.id.tvStatus)

    var index = 0
    var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null
    var onBlockClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    init {
        view.get()?.let {

            it.setOnClickListener {
                if (view.get()?.scrollX != 0) {
                    view.get()?.scrollTo(0, 0)
                }

            }

        }
    }

    fun bindView(user: User){
        id.text = "id: " + user.id.toString()
        name.text = user.name
        email.text = user.email
        registrationDate.text = "Registration date: " + user.registrationDate
        lastLogin.text = "Last login: " + user.lastLogin
        status.text = when(user.status){
            true -> "Blocked"
            else -> "Not Blocked"
        }
    }

    fun updateView() {
        view.get()?.scrollTo(0, 0)
    }
}