package com.example.estechapp.data.models

data class DataFreeUsageModel(
    val start: String,
    val end: String,
    val status: String,
    val room: RoomId,
    val user: UserId
)
