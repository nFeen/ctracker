package com.example.ctracker.repository.mock

import com.example.ctracker.entity.Food
import com.example.ctracker.repository.FoodRepository

class MockFoodRepository : FoodRepository {
    private val foods = mutableListOf<Food>()

    override fun getFoodById(id: Int): Food? {
        return foods.find { it.id == id }
    }

    override fun addFood(food: Food) {
        foods.add(food)
    }

    override fun getAllFoods(): List<Food> {
        return foods.toList()
    }
}