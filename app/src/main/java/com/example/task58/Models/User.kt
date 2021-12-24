package com.example.task58.Models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val registrationDate: String,
    val lastLogin: String,
    var status: Boolean,
    var password: String
) {
}