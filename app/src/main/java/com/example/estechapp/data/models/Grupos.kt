package com.example.estechapp.data.models

data class Grupos(
    val id: Int,
    val name: String,
    val description: String,
    val evening: Boolean,
    val year: String,
    val courseId: Int,
    val roomId: Int?,
    val users: List<UserConRol>,
    val timeTable: List<DataRoomTimeTable>,
    var cantidad: Int
)
