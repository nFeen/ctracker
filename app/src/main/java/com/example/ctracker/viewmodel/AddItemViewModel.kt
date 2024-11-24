package com.example.ctracker.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.entity.Food
import com.example.ctracker.entity.Meal
import com.example.ctracker.repository.mock.MockFoodRepository
import com.example.ctracker.repository.mock.MockMealRepository
import com.example.ctracker.repositoryBack.FoodRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.Date

class AddItemViewModel(private val index: Int, private val mealType: Int) : ViewModel() {
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1")!!.toInt()
    val food = mutableStateOf<Food?>(null)
    val weightState = mutableStateOf("50")
    val isError = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        println("bebra")
        loadFoodDetails(index)
    }

    private fun loadFoodDetails(foodId: Int) {

        isLoading.value = true
        viewModelScope.launch {
            try {
                val foodResponse = FoodRepository.getFoodById(foodId)
                if (foodResponse != null) {
                    food.value = Food(
                        id = foodResponse.food_id,
                        name = foodResponse.food,
                        calories = foodResponse.calorie,
                        protein = foodResponse.protein,
                        fat = foodResponse.fats,
                        carb = foodResponse.carbs
                    )
                } else {
                    errorMessage.value = "Продукт не найден"
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка загрузки данных: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addMealToUser() {
        val currentFood = food.value
        if (currentFood == null || isError.value) return // Прерываем, если есть ошибка или продукт не загружен

        val weight = weightState.value.toFloatOrNull() ?: 0f
        val adjustedCalories = currentFood.calories * (weight / 100)
        val adjustedProtein = currentFood.protein * (weight / 100)
        val adjustedFats = currentFood.fat * (weight / 100)
        val adjustedCarbs = currentFood.carb * (weight / 100)

        val meal = Meal(
            id = 0, // ID сгенерируется в репозитории
            mealType = mealType,
            name = currentFood.name,
            calories = adjustedCalories,
            carbs = adjustedCarbs,
            fats = adjustedFats,
            protein = adjustedProtein,
            quantity = weight,
            date = Date()
        )
        // MealRepository.addMeal(userId, meal) - Добавление логики для MealRepository позже
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