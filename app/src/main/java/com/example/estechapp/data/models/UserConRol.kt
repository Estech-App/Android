package com.example.estechapp.data.models

data class UserConRol(
    val id: Int,
    val email: String,
    val name: String,
    val lastname: String,
    val role: String,
    var posicion: Int
)
