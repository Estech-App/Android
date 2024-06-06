package com.example.estechapp.data.models

data class DataMentoringResponse(
    val id: Int? = null,
    val start: String? = null,
    val end: String? = null,
    val roomId: Int? = null,
    val status: String? = null,
    val teacher: UserFull? = null,
    val student: UserFull? = null,
    var roomName: String? = null,
    var studentAndroid: Boolean? = null
)
