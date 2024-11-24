package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Meal
import com.example.ctracker.repository.mock.MockMealRepository

class EditMealViewModel(private val mealId: Int) : ViewModel() {
    val meal: Meal? = MockMealRepository.getMealById(mealId)
    val weightState = mutableStateOf(meal?.quantity?.toInt().toString())
    val isError = mutableStateOf(false)

    fun updateMeal() {
        val updatedWeight = weightState.value.toFloatOrNull()
        if (updatedWeight == null || updatedWeight <= 0) {
            isError.value = true
            return
        }
        meal?.let {
            it.quantity = updatedWeight
            it.calories = it.calories / it.quantity * updatedWeight
            it.protein = it.protein / it.quantity * updatedWeight
            it.fats = it.fats / it.quantity * updatedWeight
            it.carbs = it.carbs / it.quantity * updatedWeight
            MockMealRepository.updateMeal(it)
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
        isError.value = validatedWeight.isEmpty() || validatedWeight.toFloatOrNull()?.let { it <= 0f } ?: true
    }
}
