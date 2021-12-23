package com.example.task58.database

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val registrationDate: String,
    val lastLogin: String,
    var status: Boolean
) {}