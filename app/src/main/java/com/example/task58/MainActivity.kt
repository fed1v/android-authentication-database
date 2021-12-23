package com.example.task58

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task58.database.User
import com.example.task58.database.DataBaseHelper
import com.example.task58.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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


    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            getUsers()
        } else{
            Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
        }
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
}