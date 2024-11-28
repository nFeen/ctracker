package com.example.ctracker.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.ApiService.AddMealRequest
import com.example.ctracker.entity.Food
import com.example.ctracker.entity.Meal
import com.example.ctracker.repositoryBack.FoodRepository
import com.example.ctracker.repositoryBack.MealRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddItemViewModel(private val index: Int, private val mealType: Int) : ViewModel() {
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1").toInt()
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
                        name = foodResponse.name,
                        calories = foodResponse.calorie.toFloat(),
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
        val quantity = weight.toInt() // Преобразуем вес в целое число для запроса

        // Получаем текущую дату в формате yyyy-MM-dd
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormatter.format(Date())

        // Создаем запрос для добавления приема пищи
        val addMealRequest = AddMealRequest(
            user_id = userId,
            food_id = currentFood.id,
            quantity = quantity,
            date = currentDate,
            part_of_the_day = mealType // Используем mealType как part_of_the_day
        )

        viewModelScope.launch {
            try {
                val success = MealRepository.addMeal(addMealRequest)
                if (success) {
                    println("Прием пищи успешно добавлен")
                } else {
                    println("Не удалось добавить прием пищи")
                }
            } catch (e: Exception) {
                println("Ошибка при добавлении приема пищи: ${e.message}")
            }
        }
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