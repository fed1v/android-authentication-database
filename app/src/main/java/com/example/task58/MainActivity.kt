package com.example.task58

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
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
    private var adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        dataBaseHelper = DataBaseHelper(this)

        showUsers()

        btnAdd.setOnClickListener {
            addUser()
            showUsers()
        }

        btnViewAll.setOnClickListener {
            showUsers()
        }


    }

    private fun initView(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        btnAdd = findViewById(R.id.btn_add)
        btnViewAll = findViewById(R.id.btn_viewAll)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        swStatus = findViewById(R.id.sw_status)
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
            Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUsers(){
        val userList = dataBaseHelper.getAllUsers()

        //TODO

    //    adapter = Adapter(this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone())
        binding.recyclerView.adapter = adapter
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}