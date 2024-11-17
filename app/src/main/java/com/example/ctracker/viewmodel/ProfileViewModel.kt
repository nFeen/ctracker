package com.example.ctracker.viewmodel

import SharedPreferencesManager
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.CTrackerApp
import com.example.ctracker.entity.User
import com.example.ctracker.repository.mock.MockMealRepository
import com.example.ctracker.repository.mock.MockUserRepository
import java.util.Calendar
import java.util.Date

class ProfileViewModel(
    private val userRepository: MockUserRepository = MockUserRepository,
    private val mealRepository: MockMealRepository = MockMealRepository
) : ViewModel() {

    // Получение ID пользователя из SharedPreferences
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1").toInt()

    // Пользовательские данные
    private val user: User? = userRepository.getUserById(userId)

    // Макросы и калории
    val fats = mutableIntStateOf(0)
    val carbs = mutableIntStateOf(0)
    val protein = mutableIntStateOf(0)
    val maxCalorie = mutableIntStateOf(user?.maxCalorie ?: 2500)
    val calorie = mutableIntStateOf(user?.currentCalorie ?: 0)

    // Логин пользователя
    val userName = mutableStateOf(user?.login ?: "Unknown User")

    // Вес пользователя
    val userWeight = mutableStateOf("${user?.weight ?: 75} kg")

    // Данные для графика
    val chartData = mutableListOf(
        1870 to "20.11",
        20000 to "20.11",
        2000 to "20.11",
        1200 to "20.11",
        1800 to "20.11",
        2100 to "20.11",
        1600 to "20.11"
    )

    init {
        calculateMacrosFromMeals()
        SharedPreferencesManager.init(CTrackerApp.applicationContext())
    }

    // Подсчет данных макросов (жиров, белков, углеводов) по сегодняшним приемам пищи
    private fun calculateMacrosFromMeals() {
        val todayMeals = mealRepository.getMealsForUser(userId).filter { isToday(it.date) }

        fats.value = todayMeals.sumOf { it.fats.toInt() }
        carbs.value = todayMeals.sumOf { it.carbs.toInt() }
        protein.value = todayMeals.sumOf { it.protein.toInt() }
    }

    // Проверяет, является ли дата сегодняшней
    private fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        calendar.time = date

        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
    }
}
