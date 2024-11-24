package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Food
import com.example.ctracker.entity.Meal
import com.example.ctracker.repository.mock.MockFoodRepository
import com.example.ctracker.repository.mock.MockMealRepository
import java.util.Date

class AddItemViewModel(private val index: Int, private val mealType: Int) : ViewModel() {
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1")!!.toInt()
    val food: Food = MockFoodRepository.getByID(index)
    val weightState = mutableStateOf("50")
    val isError = mutableStateOf(false)

    fun addMealToUser() {
        if (isError.value) return // Прерываем, если есть ошибка

        val adjustedCalories = food.calories * (weightState.value.toFloat() / 100)
        val adjustedProtein = food.protein * (weightState.value.toFloat() / 100)
        val adjustedFats = food.fat * (weightState.value.toFloat() / 100)
        val adjustedCarbs = food.carb * (weightState.value.toFloat() / 100)

        val meal = Meal(
            id = 0, // ID сгенерируется в репозитории
            mealType = mealType,
            name = food.name,
            calories = adjustedCalories,
            carbs = adjustedCarbs,
            fats = adjustedFats,
            protein = adjustedProtein,
            quantity = weightState.value.toFloatOrNull() ?: 0f,
            date = Date()
        )
        MockMealRepository.addMeal(userId, meal)
    }

    fun updateWeight(input: String) {
        val sanitizedInput = input.toIntOrNull() // Пробуем преобразовать в число
        val validatedWeight = when {
            sanitizedInput == null -> ""
            sanitizedInput < 0 -> "0"
            sanitizedInput > 10000 -> "10000"
            else -> sanitizedInput.toString()
        }
        weightState.value = validatedWeight

        // Проверка на ошибку
        isError.value = validatedWeight.isEmpty() || validatedWeight.toFloatOrNull()?.let { it <= 0f } ?: true
    }
}

