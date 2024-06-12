package com.example.estechapp.data.models

data class DataTimeTableResponse(
    val id: Int,
    val schoolGroupId: Int,
    val moduleId: Int,
    val start: String,
    val end: String,
    val weekday: String,
    var groupName: String,
    var moduleName: String
)
