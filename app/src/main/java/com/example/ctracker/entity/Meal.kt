package com.example.ctracker.entity

data class Meal(
    val id: Int,
    val foodId: Int,
    val name: String,
    val calories: Float,
    val fat: Float,
    val carb: Float,
    val protein: Float,
    val quantity: Float
)
