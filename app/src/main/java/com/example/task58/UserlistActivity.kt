package com.example.task58

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.Models.User
import com.example.task58.databinding.ActivityUserlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserlistActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private var adapter: UserFirebaseAdapter? = null
    private lateinit var binding: ActivityUserlistBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlist)
        
        initView()
        initData()

        getUserData()

        adapter?.setOnClickDeleteItem {
            deleteUser(it.id!!)
            getUserData()
        }

        adapter?.setOnClickBlockItem {
            println("Block...(In UserlistActivity)")
            updateUserStatus()
            getUserData()
        }

        setItemTouchHelper()
    }

    private fun updateUserStatus() {
        //TODO

        println("Update User Status")
    }

    private fun deleteUser(id: String) {
        db.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData(){
        auth = FirebaseAuth.getInstance()
        userArrayList = arrayListOf<User>()
        db = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun initView(){
        userRecyclerView = findViewById(R.id.userList)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserFirebaseAdapter()
        userRecyclerView.adapter = adapter
    }

    private fun getUserData() {
        db.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userArrayList.removeAll{true}
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }

                    adapter?.addItems((userArrayList))
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(100f, this@UserlistActivity)
            var currentScrollX = 0
            var currentScrollXWhenInActive = 0
            var initXWhenInActive = 0f
            var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }

                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)


                    } else {
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),
                                0
                            )
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(userRecyclerView)
        }
    }

    fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }
}