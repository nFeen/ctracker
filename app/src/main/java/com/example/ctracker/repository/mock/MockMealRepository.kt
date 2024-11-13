package com.example.ctracker.repository.mock

import com.example.ctracker.entity.Meal
import com.example.ctracker.repository.MealRepository

class MockMealRepository : MealRepository {
    private val meals = mutableListOf<Meal>()

    override fun getMealById(id: Int): Meal? {
        return meals.find { it.id == id }
    }

    override fun addMeal(meal: Meal) {
        meals.add(meal)
    }

    override fun getAllMeals(): List<Meal> {
        return meals.toList()
    }
}