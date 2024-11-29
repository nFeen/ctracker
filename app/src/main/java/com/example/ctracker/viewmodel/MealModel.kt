package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import com.example.ctracker.entity.Meal

data class MealModel(
    val name: String,
    val productList: List<Meal>,
    var isProductListVisible: MutableState<Boolean>,
    val toggleProductList: () -> Unit
) {
    val totalCalories: Int
        get() = productList.sumOf { it.calories.toInt() }
}