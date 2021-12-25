package com.example.task58

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.database.User

class UserAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var userList: ArrayList<User> = ArrayList()
    private var onClickDeleteItem: ((User) -> Unit)? = null
    private var onClickBlockItem: ((User) -> Unit)? = null

    fun addItems(items: ArrayList<User>){
        userList = items
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback: (User) -> Unit){
        this.onClickDeleteItem = callback
    }

    fun setOnClickBlockItem(callback: (User) -> Unit){
        this.onClickBlockItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = userList[position]
        holder.bindView(user)

        holder.textViewDelete.setOnClickListener {
            println("Delete in Adapter")

            onClickDeleteItem?.invoke(user)
            println("After onClickDeleteItem invoke(user)")
            notifyItemRemoved(position)
        }

        holder.textViewBlock.setOnClickListener {
            if(user.status == true){
                user.status = false
            } else {
                user.status = true
            }

            println("Block in Adapter")

            onClickBlockItem?.invoke(user)
            println("After onClickBlockItem invoke(user)")

            notifyItemChanged(position)
        }

        holder.updateView()
    }

    override fun getItemCount(): Int = userList.size

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        userList.removeAt(position)
        notifyItemRemoved(position)
    }

}
