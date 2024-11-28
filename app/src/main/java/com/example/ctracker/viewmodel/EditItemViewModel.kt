package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.entity.Food
import com.example.ctracker.entity.Meal
import com.example.ctracker.repositoryBack.FoodRepository
import com.example.ctracker.repositoryBack.MealRepository
import kotlinx.coroutines.launch


class EditMealViewModel(private val mealId: Int) : ViewModel() {
    val weightState = mutableStateOf("")
    val food = mutableStateOf<Food?>(null)
    val isError = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadMealDetails()
    }

    private fun loadMealDetails() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                // Получаем данные о приеме пищи
                val mealResponse = MealRepository.getMealById(mealId)
                if (mealResponse != null) {
                    weightState.value = mealResponse.quantity.toString()

                    // Загружаем данные о продукте по food_id
                    val foodResponse = FoodRepository.getFoodById(mealResponse.food_id)
                    if (foodResponse != null) {
                        food.value = Food(
                            id = foodResponse.food_id,
                            name = foodResponse.name,
                            calories = foodResponse.calorie.toFloat(),
                            fats = foodResponse.fats,
                            carbs = foodResponse.carbs,
                            protein = foodResponse.protein
                        )
                    } else {
                        errorMessage.value = "Продукт не найден"
                    }
                } else {
                    errorMessage.value = "Прием пищи не найден"
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка загрузки данных: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateMeal() {
        val updatedWeight = weightState.value.toIntOrNull()
        if (updatedWeight == null || updatedWeight <= 0) {
            isError.value = true
            return
        }

        viewModelScope.launch {
            try {
                val success = MealRepository.editMeal(mealId, updatedWeight)
                if (success) {
                    println("Прием пищи успешно обновлен")
                } else {
                    errorMessage.value = "Не удалось обновить прием пищи"
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка при обновлении приема пищи: ${e.message}"
            }
        }
    }

    fun deleteMeal() {
        println("Try to delete")
        viewModelScope.launch {
            try {
                val success = MealRepository.deleteMeal(mealId)
                if (success) {
                    println("deleted succesfull")
                } else {
                    println("delete not succes")
                    errorMessage.value = "Не удалось удалить прием пищи"
                }
            } catch (e: Exception) {
                errorMessage.value = "delete error: ${e.message}"
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
        isError.value = validatedWeight.isEmpty() || validatedWeight.toIntOrNull()?.let { it <= 0 } ?: true
    }
}
