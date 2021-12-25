package com.example.task58

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.database.User
import com.example.task58.database.DataBaseHelper
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var user: User? = null

    private lateinit var btnAdd: Button
    private lateinit var btnViewAll: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var swStatus: SwitchCompat

    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        dataBaseHelper = DataBaseHelper(this)

        getUsers()

        btnAdd.setOnClickListener {
            addUser()
            getUsers()
        }

        btnViewAll.setOnClickListener {
            getUsers()
        }

        adapter?.setOnClickDeleteItem {
            println("Delete in Main")
            deleteUser(it.id)
            getUsers()
        }

        adapter?.setOnClickBlockItem {
            println("Block in Main")
            updateUser(it)
            getUsers()
        }

        setItemTouchHelper()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        btnAdd = findViewById(R.id.btn_add)
        btnViewAll = findViewById(R.id.btn_viewAll)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        swStatus = findViewById(R.id.sw_status)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun addUser(){
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val registrationDate = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
        val lastLogin = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
        val status = swStatus.isChecked

        val user: User

        if (name == "" || email == "") {
            user = User(-1, "error", "error@error.error", "errorReg", "errorLog", false)
        } else{
            user = User(-1, name, email, registrationDate, lastLogin, status)
        }

        dataBaseHelper = DataBaseHelper(this)
        val success = dataBaseHelper.insertUser(user)
        if(success){
            Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUser(id: Int){
        if(id == null) return
        dataBaseHelper.deleteUserById(id)
    }

    private fun updateUser(user: User){
        dataBaseHelper.updateUser(user)
    }

    private fun getUsers(){
        val userList = dataBaseHelper.getAllUsers()
        adapter?.addItems(userList)
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(100f, this@MainActivity)
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
            attachToRecyclerView(recyclerView)
        }
    }

    fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

}