package com.example.estechapp.data.models

data class DataMentoringResponse(
    val id: Int,
    val date: String,
    val roomId: Int,
    val status: String,
    val teacherId: Int,
    val studentId: Int
)
