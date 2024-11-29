package com.example.ctracker.entity

data class Meal(
    val id: Int,
    val mealType: Int, // Тип приема пищи: 0 - Завтрак, 1 - Обед, 2 - Ужин, 3 - Другое
    val name: String,
    var calories: Float,
    var protein: Float,
    var fats: Float,
    var carbs: Float,
    var quantity: Int,
    val date: String
)
