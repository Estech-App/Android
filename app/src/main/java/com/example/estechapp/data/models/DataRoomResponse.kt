package com.example.estechapp.data.models

data class DataRoomResponse(
    val id: Int,
    var name: String,
    val description: String,
    val mentoringRoom: Boolean,
    val studyRoom: Boolean,
    val timeTables: List<DataRoomTimeTable>
)
