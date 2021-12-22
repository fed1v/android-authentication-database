package com.example.task58.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MY_TEST_DATABASE"
        private val TABLE_NAME = "CUSTOMER_TABLE"
        private val NAME_COL = "CUSTOMER_NAME"
        private val AGE_COL = "CUSTOMER_AGE"
        private val ACTIVE_CUSTOMER_COL = "ACTIVE_CUSTOMER  "
        private val ID_COL = "id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME +
                "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                AGE_COL + " INT, " + ACTIVE_CUSTOMER_COL + " BOOL" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addOne(customerModel: CustomerModel): Boolean {
        val values = ContentValues()
        values.put(DataBaseHelper.NAME_COL, customerModel.name)
        values.put(DataBaseHelper.AGE_COL, customerModel.age)
        values.put(DataBaseHelper.ACTIVE_CUSTOMER_COL, customerModel.isActive)

        val db = this.writableDatabase
        val insert = db.insert(DataBaseHelper.TABLE_NAME, null, values)

        db.close()

        if (insert == -1L) return false
        return true;
    }

    fun getEveryone(): List<CustomerModel> {
        val resultList = ArrayList<CustomerModel>()
        val query = "SELECT * FROM " + TABLE_NAME
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val age = cursor.getInt(2)
                val isActive = when (cursor.getInt(3)) {
                    0 -> false
                    else -> true
                }

                val customerModel = CustomerModel(id, name, age, isActive)
                resultList.add(customerModel)

            } while (cursor.moveToNext())

        } else {
        }

        cursor.close()
        db.close()

        return resultList
    }

    fun deleteOne(customerModel: CustomerModel): Boolean{
        val db = this.writableDatabase
        val query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL + " = " + customerModel.id
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            return true
        }

        return false
    }
}