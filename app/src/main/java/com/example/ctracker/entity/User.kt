package com.example.ctracker.entity

data class User(
    val id: Int,
    val login: String,
    val mealsId: List<Int>,
    val password: String
)
