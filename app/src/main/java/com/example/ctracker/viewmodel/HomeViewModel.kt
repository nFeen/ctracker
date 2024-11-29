package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Meal
import androidx.lifecycle.viewModelScope
import com.example.ctracker.SharedPreferencesManager
import com.example.ctracker.repositoryBack.FoodRepository
import com.example.ctracker.repositoryBack.MealRepository
import com.example.ctracker.repositoryBack.UserRepository
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val userId: Int = SharedPreferencesManager.getString("userID", "-1").toInt()
    val maxCalories = mutableIntStateOf(SharedPreferencesManager.getInt("maxCalorie", 2500))
    val calorie = mutableIntStateOf(SharedPreferencesManager.getInt("calorie", 0))
    val recommendations = mutableStateOf(SharedPreferencesManager.getString("recommendation", ""))

    val mealList = mutableListOf<MealModel>()
    val isLoading: MutableState<Boolean> = mutableStateOf(true)


    init {
        initializeEmptyMealList()
        loadMeals()
    }

    private fun initializeEmptyMealList() {
        mealList.apply {
            clear()
            add(MealModel("Завтрак", emptyList(), mutableStateOf(false)) { toggleVisibility(0) })
            add(MealModel("Обед", emptyList(), mutableStateOf(false)) { toggleVisibility(1) })
            add(MealModel("Ужин", emptyList(), mutableStateOf(false)) { toggleVisibility(2) })
            add(MealModel("Другое", emptyList(), mutableStateOf(false)) { toggleVisibility(3) })
        }
    }

    private fun loadMeals() {
        viewModelScope.launch {
            try {

                val mealResponses = MealRepository.getMeals(userId, getCurrentDate())

                if (mealResponses.isEmpty()) {
                    calorie.intValue = 0
                    return@launch
                }

                val userMeals = mealResponses.map { response ->
                    val foodName = try {
                        val food = FoodRepository.getFoodById(response.food_id)
                        food?.name
                            ?: "Неизвестный продукт" // Если продукт не найден, подставляем заглушку
                    } catch (e: Exception) {
                        "Неизвестный продукт"
                    }

                    Meal(
                        id = response.meal_id,
                        mealType = response.part_of_the_day,
                        name = foodName, // Используем полученное имя продукта
                        calories = response.calories,
                        carbs = response.carbs,
                        fats = response.fats,
                        protein = response.protein,
                        quantity = response.quantity,
                        date = getCurrentDate() // Используем текущую дату или response.date, если оно есть
                    )
                }

                // Группируем приемы пищи по типу
                val breakfastProducts = userMeals.filter { it.mealType == 0 }
                val lunchProducts = userMeals.filter { it.mealType == 1 }
                val dinnerProducts = userMeals.filter { it.mealType == 2 }
                val additionalProducts = userMeals.filter { it.mealType == 3 }

                // Наполняем MealModel полученными данными
                mealList.apply {
                    clear()
                    add(
                        MealModel(
                            "Завтрак",
                            breakfastProducts,
                            mutableStateOf(false)
                        ) { toggleVisibility(0) })
                    add(
                        MealModel(
                            "Обед",
                            lunchProducts,
                            mutableStateOf(false)
                        ) { toggleVisibility(1) })
                    add(MealModel("Ужин", dinnerProducts, mutableStateOf(false)) {
                        toggleVisibility(
                            2
                        )
                    })
                    add(
                        MealModel(
                            "Другое",
                            additionalProducts,
                            mutableStateOf(false)
                        ) { toggleVisibility(3) })
                }

                // Обновляем текущие калории
                calorie.intValue = userMeals.sumOf { it.calories.toInt() }
                SharedPreferencesManager.saveInt("calorie", calorie.intValue)
            } catch (e: Exception) {
                e.printStackTrace()
                // В случае ошибки сбрасываем MealModel в пустые
                initializeEmptyMealList()
            } finally {
                isLoading.value = false
            }
        }
    }

    // Переключение видимости для определенного приема пищи
    private fun toggleVisibility(index: Int) {
        mealList[index].isProductListVisible.value = !mealList[index].isProductListVisible.value
    }

    // Получение текущей даты в формате yyyy-MM-dd
    private fun getCurrentDate(): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }

    fun getRecommendations() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val recommendationText =
                    UserRepository.getRecommendationPrompt(userId, getCurrentDate())
                recommendations.value = recommendationText
                SharedPreferencesManager.saveString("recommendation", recommendationText)
            } catch (e: Exception) {
                recommendations.value = "Не удалось получить рекомендации. Попробуйте позже."
            } finally {
                isLoading.value = false
            }
        }
    }
}


