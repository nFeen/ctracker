package com.example.ctracker.entity


data class User(
    val id: Int,
    val login: String,
    val mealsId: List<Int>,
    val password: String,
    val maxCalorie: Int = 2000,
    var currentCalorie: Int = 0,
    val weight: Int = 75,
    val height: Int = 180,
    val avatarBase64: String? = null // Поле для аватарки в формате Base64
)