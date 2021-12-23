package com.example.task58

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.task58.database.User
import com.example.task58.database.DataBaseHelper
import com.example.task58.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dataBaseHelper: DataBaseHelper
    var adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dataBaseHelper = DataBaseHelper(this)
        setContentView(binding.root)

        showCustomers()

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val registrationDate = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
            val lastLogin = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
            val status = binding.swStatus.isChecked

            val user: User

            if (name == "" || email == "") {
                user = User(-1, "error", "error@error.error", "errorReg", "errorLog", false)
            } else{
                user = User(-1, name, email, registrationDate, lastLogin, status)
            }

            dataBaseHelper = DataBaseHelper(this)
            dataBaseHelper.addOne(user)

            showCustomers()
        }

        binding.btnViewAll.setOnClickListener {
            showCustomers()
        }

    }

    fun showCustomers(){
    //    adapter = Adapter(this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone())
        binding.recyclerView.adapter = adapter
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}