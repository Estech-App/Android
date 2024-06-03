package com.example.estechapp.data.models

data class DataMentoringResponse(
    val id: Int,
    val start: String,
    val end: String,
    val roomId: Int,
    val status: String,
    val teacher: UserFull,
    val student: UserFull,
    var roomName: String,
    var studentAndroid: Boolean
)
