package com.example.todo.fragments

data class TimeApiResponse(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val seconds: Int,
    val dateTime: String,
    val date: String,
    val time: String,
    val timeZone: String,
    val dayOfWeek: String,
    val dstActive: Boolean
)
