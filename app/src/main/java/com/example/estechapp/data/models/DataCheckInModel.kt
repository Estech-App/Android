package com.example.estechapp.data.models

import java.util.Date

data class DataCheckInModel(
    val date: Date,
    val checkIn: Boolean,
    val user: User
)
