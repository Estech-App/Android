package com.example.estechapp.data.models

data class DataModuleResponse(
    val id: Int,
    val year: Int,
    val name: String,
    val acronym: String,
    val courseAcronym: String,
    val usersName: List<String>
)
