package com.example.estechapp.data.models

import java.util.Date

data class DataCheckInModel(
    val date: String,
    val checkIn: Boolean,
    val user: User
)
