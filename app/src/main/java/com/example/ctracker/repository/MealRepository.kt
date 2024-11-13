package com.example.ctracker.repository

import com.example.ctracker.entity.Meal

interface MealRepository {
    fun getMealById(id: Int): Meal?
    fun addMeal(meal: Meal)
    fun getAllMeals(): List<Meal>
}