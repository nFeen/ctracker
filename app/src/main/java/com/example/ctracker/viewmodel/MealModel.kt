package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import com.example.ctracker.entity.Meal

data class MealModel(
    val name: String, // Название приема пищи
    val productList: List<Meal>, // Список продуктов
    var isProductListVisible: MutableState<Boolean>, // Булевое значение для видимости списка продуктов
    val toggleProductList: () -> Unit // Функция для переключения видимости
) {
    val totalCalories: Int
        get() = productList.sumOf { it.calories.toInt() } // Суммируем калории всех продуктов
}