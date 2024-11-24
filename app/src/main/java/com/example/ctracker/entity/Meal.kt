package com.example.ctracker.entity

import java.util.Date

data class Meal(
    val id: Int,
    val mealType: Int, // Тип приема пищи: 0 - Завтрак, 1 - Обед, 2 - Ужин, 3 - Другое
    val name: String,
    var calories: Float,
    var protein: Float,
    var fats: Float,
    var carbs: Float,
    var quantity: Float,
    val date: Date // Дата, когда был употреблен прием пищи
)
