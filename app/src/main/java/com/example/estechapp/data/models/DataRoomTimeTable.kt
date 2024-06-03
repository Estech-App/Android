package com.example.estechapp.data.models

data class DataRoomTimeTable(
    val status: String,
    val start: String,
    val end: String,
    val dayOfWeek: String,
    val reccurence: Boolean,
    val roomId: Int
)
