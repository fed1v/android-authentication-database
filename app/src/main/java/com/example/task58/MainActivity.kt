package com.example.task58

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.task58.database.CustomerModel
import com.example.task58.database.DataBaseHelper
import com.example.task58.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var customerArrayAdapter: ArrayAdapter<CustomerModel>
    lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dataBaseHelper = DataBaseHelper(this)
        setContentView(binding.root)

        showCustomers()

        binding.buttonSwitchActivity2.setOnClickListener {
            switchActivities()
        }

        binding.btnAdd.setOnClickListener {
            val age = binding.etAge.text.toString().toIntOrNull()
            val name = binding.etName.text.toString()
            val isChecked = binding.swActive.isChecked

            val customerModel: CustomerModel

            if (age == null || name == "") {
                customerModel = CustomerModel(-1, "error", 0, false)
            } else{
                customerModel = CustomerModel(-1, name, age, isChecked)
            }

            dataBaseHelper = DataBaseHelper(this)
            dataBaseHelper.addOne(customerModel)

            showCustomers()
        }

        binding.btnViewAll.setOnClickListener {
            showCustomers()
        }

        binding.lvCustomerList.setOnItemClickListener {parent, view, position, id ->
            val clickedCustomer: CustomerModel = parent.getItemAtPosition(position) as CustomerModel
            dataBaseHelper.deleteOne(clickedCustomer)
            showCustomers()
        }

    }

    fun switchActivities() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

    fun showCustomers(){
        customerArrayAdapter = ArrayAdapter<CustomerModel>(this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone())
        binding.lvCustomerList.adapter = customerArrayAdapter
    }

}