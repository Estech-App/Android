package com.example.estechapp.data.models

data class DataLoginResponse(
    val roles: List<Roles>,
    val message: String,
    val token: String,
    val username: String
)
