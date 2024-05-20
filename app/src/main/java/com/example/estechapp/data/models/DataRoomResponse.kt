package com.example.estechapp.data.models

data class DataRoomResponse(
    val id: Int,
    val name: String,
    val description: String,
    val mentoringRoom: Boolean,
    val studyRoom: Boolean
)
