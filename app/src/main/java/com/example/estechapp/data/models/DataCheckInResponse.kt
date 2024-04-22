package com.example.estechapp.data.models

import java.util.Date

data class DataCheckInResponse(
    val date: Date,
    val checkIn: Boolean,
    val user: String,
    val userId: Int
)
