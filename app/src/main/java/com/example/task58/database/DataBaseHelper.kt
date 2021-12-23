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
        private val DATABASE_NAME = "USER_DATABASE"
        private val TABLE_NAME = "USER_TABLE"
        private val ID_COL = "ID"
        private val COL_NAME = "USER_NAME"
        private val COL_EMAIL = "USER_EMAIL"
        private val COL_REGISTRATION_DATE = "REGISTRATION_DATE"
        private val COL_LAST_LOGIN = "LAST_LOGIN"
        private val COL_STATUS = "STATUS"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME +
                "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " INT, " +
                COL_REGISTRATION_DATE + " TEXT, " +
                COL_LAST_LOGIN + " TEXT, " +
                COL_STATUS + " BOOL" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user: User): Boolean {
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_EMAIL, user.email)
        values.put(COL_REGISTRATION_DATE, user.registrationDate)
        values.put(COL_LAST_LOGIN, user.lastLogin)
        values.put(COL_STATUS, user.status)

        val db = this.writableDatabase
        val insert = db.insert(TABLE_NAME, null, values)

        db.close()

        return insert != -1L;
    }

    fun getAllUsers(): ArrayList<User> {
        val resultList = ArrayList<User>()
        val query = "SELECT * FROM " + TABLE_NAME
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val email = cursor.getString(2)
                val registrationDate = cursor.getString(3)
                val lastLogin = cursor.getString(4)
                val status = when (cursor.getInt(5)) {
                    0 -> false
                    else -> true
                }

                val user = User(id, name, email, registrationDate, lastLogin, status)
                resultList.add(user)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return resultList
    }

    fun deleteOne(user: User): Boolean{
        val db = this.writableDatabase
        val query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL + " = " + user.id
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            return true
        }

        return false
    }
}