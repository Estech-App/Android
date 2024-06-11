package com.example.estechapp.data.models

data class UserFullVerdat(
    val id: Int,
    val email: String,
    val name: String,
    val lastname: String,
    val role: String,
    val groups: List<Grupos>
)
