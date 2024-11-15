package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Meal

class HomeViewModel : ViewModel() {
    // Список продуктов для разных приемов пищи
    private val breakfastProducts = listOf(
        Meal(1, 1, "zalupa", 70F, 6.0F, 5.0F, 1.0F, 10.0F),
        Meal(2, 2, "zalupa", 4.0F, 2.0F, 21.0F, 20F, 10.0F)
    )

    private val lunchProducts = listOf(
        Meal(3, 3, "zalupa", 20.0F, 12.0F, 35.0F, 50F, 10.0F),
        Meal(4, 4, "zalupa", 3.0F, 6.0F, 8.0F, 15F, 10.0F)
    )

    private val dinnerProducts = listOf(
        Meal(5, 5, "zalupa", 35.0F, 25.0F, 0.0F, 2F, 10.0F),
        Meal(6, 6, "zalupa", 4.0F, 0.5F, 45.0F, 40F, 10.0F)
    )
    private val additionalProducts = listOf(
        Meal(5, 5, "zalupa", 35.0F, 25.0F, 0.0F, 2F, 10.0F),
        Meal(6, 6, "zalupa", 4.0F, 0.5F, 45.0F, 40F, 10.0F)
    )

    // Максимальные калории
    var maxCalories = mutableStateOf(2000)

    // Текущие калории
    var calorie = mutableStateOf(3000)

    // Список моделей для каждого приема пищи
    val mealList = listOf(
        MealModel("Завтрак", breakfastProducts, mutableStateOf(true)) { toggleVisibility(0) },
        MealModel("Обед", lunchProducts, mutableStateOf(false)) { toggleVisibility(1) },
        MealModel("Ужин", dinnerProducts, mutableStateOf(false)) { toggleVisibility(2) },
        MealModel("Другое", dinnerProducts, mutableStateOf(false)) { toggleVisibility(3) }
    )

    // Переключение видимости для определенного приема пищи
    private fun toggleVisibility(index: Int) {
        mealList[index].isProductListVisible.value = !mealList[index].isProductListVisible.value
    }

    // Обновление текущего значения калорий
    fun updateCalories(newCalories: Int) {
        calorie.value = newCalories
    }
}

data class MealModel(
    val name: String, // Название приёма пищи
    val productList: List<Meal>, // Список продуктов
    var isProductListVisible: MutableState<Boolean>, // Булевое значение для видимости списка продуктов
    val toggleProductList: () -> Unit // Функция для переключения видимости
)