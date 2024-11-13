package com.example.ctracker.entity

data class Meal(
    val id: Int,
    val foodId: Int,
    val calories: Int,
    val fat: Float,
    val carb: Float,
    val protein: Float,
    val quantity: Float
)
