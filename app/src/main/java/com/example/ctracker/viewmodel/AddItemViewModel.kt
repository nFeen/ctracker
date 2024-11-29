package com.example.ctracker.viewmodel

import AddMealRequest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.SharedPreferencesManager
import com.example.ctracker.entity.Food
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ctracker.repositoryBack.FoodRepository
import com.example.ctracker.repositoryBack.MealRepository

class AddItemViewModel(val index: Int, val mealType: Int) : ViewModel() {
    private val userId: Int = SharedPreferencesManager.getString("userID", "-1").toInt()
    val food = mutableStateOf<Food?>(null)
    val weightState = mutableStateOf("50")
    val isError = mutableStateOf(false)
    private val isLoading = mutableStateOf(false)
    private val errorMessage = mutableStateOf<String?>(null)

    init {
        loadFoodDetails(index)
    }

    private fun loadFoodDetails(foodId: Int) {

        isLoading.value = true
        viewModelScope.launch {
            try {
                val foodResponse = FoodRepository.getFoodById(foodId)
                if (foodResponse != null) {
                    food.value = Food(
                        id = foodResponse.foodId,
                        name = foodResponse.name,
                        calories = foodResponse.calorie,
                        protein = foodResponse.protein,
                        fats = foodResponse.fats,
                        carbs = foodResponse.carbs
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
        if (currentFood == null || isError.value) return

        val weight = weightState.value.toFloatOrNull() ?: 0f
        val quantity = weight.toInt()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormatter.format(Date())

        val addMealRequest = AddMealRequest(
            user_id = userId,
            food_id = currentFood.id,
            quantity = quantity,
            date = currentDate,
            part_of_the_day = mealType
        )

        viewModelScope.launch {
            try {
                MealRepository.addMeal(addMealRequest)
            } catch (e: Exception) {
                println("Ошибка при добавлении приема пищи: ${e.message}")
            }
        }
    }

    fun updateWeight(input: String) {
        val sanitizedInput = input.toIntOrNull()
        val validatedWeight = when {
            sanitizedInput == null -> ""
            sanitizedInput < 0 -> "0"
            sanitizedInput > 10000 -> "10000"
            else -> sanitizedInput.toString()
        }
        weightState.value = validatedWeight

        // Проверка на ошибку
        isError.value =
            validatedWeight.isEmpty() || validatedWeight.toFloatOrNull()?.let { it <= 0f } ?: true
    }
}