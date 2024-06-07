package com.example.estechapp.data.models

data class DataMentoringModel(
    val start: String,
    val end: String,
    val roomId: Int?,
    val status: String,
    val teacher: UserId,
    val student: UserId
)
