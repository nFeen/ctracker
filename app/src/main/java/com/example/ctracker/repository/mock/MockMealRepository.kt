package com.example.ctracker.repository.mock

import com.example.ctracker.entity.Meal
import java.util.Date
import java.util.Calendar

object MockMealRepository {
    private val meals = mutableListOf(
        Meal(1, 0, "Яичница", 70F, 6.0F, 5.0F, 1.0F, 10.0F, getDate(0)), // Завтрак, вчера
        Meal(2, 0, "Тост", 120F, 4.0F, 2.0F, 20.0F, 10.0F, getDate(0)), // Завтрак, сегодня
        Meal(3, 1, "Суп", 200F, 10.0F, 8.0F, 30.0F, 15.0F, getDate(0)), // Обед, сегодня
        Meal(4, 1, "Салат", 150F, 5.0F, 7.0F, 12.0F, 10.0F, getDate(0)), // Обед, позавчера
        Meal(5, 2, "Паста", 400F, 12.0F, 10.0F, 50.0F, 20.0F, getDate(0)), // Ужин, сегодня
        Meal(6, 2, "Курица", 250F, 30.0F, 5.0F, 2.0F, 15.0F, getDate(0)), // Ужин, сегодня
        Meal(7, 3, "Шоколад", 120F, 2.0F, 8.0F, 10.0F, 5.0F, getDate(0)), // Другое, вчера
        Meal(8, 3, "Орехи", 150F, 4.0F, 10.0F, 5.0F, 7.0F, getDate(0)) // Другое, сегодня
    )

    private val userMealsMap = mutableMapOf(
        1 to listOf(1, 2, 3, 5, 6, 8) // ID пользователя -> ID приемов пищи
    )

    // Получение списка приемов пищи для указанного пользователя
    fun getMealsForUser(userId: Int): List<Meal> {
        val mealIds = userMealsMap[userId] ?: emptyList()
        return meals.filter { it.id in mealIds }
    }

    // Генерация даты: сегодня, вчера или другое количество дней назад
    private fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
        return calendar.time
    }
}
