package com.example.ctracker.repository

import com.example.ctracker.entity.Food

interface FoodRepository {
    fun getFoodById(id: Int): Food?
    fun addFood(food: Food)
    fun getAllFoods(): List<Food>
}