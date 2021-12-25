package com.example.task58.Models

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val registrationDate: String? = null,
    val lastLogin: String? = null,
    var status: Boolean = false,
    var password: String? = null
) {
}