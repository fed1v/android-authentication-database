package com.example.task58

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.Models.User
import com.google.firebase.database.*

class UserlistActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlist)

        userRecyclerView = findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()

        getUserData()
    }

    private fun getUserData() {
        db = FirebaseDatabase.getInstance().getReference("Users")
        db.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }

                    val userFirebaseAdapter = UserFirebaseAdapter()
                    userFirebaseAdapter.userList = userArrayList
                    userRecyclerView.adapter = userFirebaseAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}