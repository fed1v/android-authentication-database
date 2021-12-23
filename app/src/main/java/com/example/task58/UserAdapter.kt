package com.example.task58

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.database.User


class UserAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var userList: ArrayList<User> = ArrayList()


    fun addItems(items: ArrayList<User>){
        userList = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = userList[position]
        holder.bindView(user)

        holder.onDeleteClick = {
            removeItem(it)
        }

    //    holder.updateView()
    }


    override fun getItemCount(): Int = userList.size

    /*fun reload(list: List<Int>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }*/

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        userList.removeAt(position)
        notifyItemRemoved(position)
    }

}