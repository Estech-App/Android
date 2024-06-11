package com.example.estechapp.data.models

data class DataFreeUsageResponse(
    val id: Int,
    val start: String,
    val end: String,
    val status: String,
    val room: DataRoomResponse,
    val user: UserFull
)
