package com.example.ctracker.repository.mock

import com.example.ctracker.entity.Food

object MockFoodRepository {
    private val foods = listOf(
        Food(1, "Яблоко", 52, 0.2f, 14f, 0.3f),
        Food(2, "Банан", 89, 0.3f, 23f, 1.1f),
        Food(3, "Куриное филе", 165, 3.6f, 0f, 31f),
        Food(4, "Овсянка", 68, 1.4f, 12f, 2.4f),
        Food(5, "Яйцо вареное", 155, 11f, 1.1f, 13f),
        Food(6, "Молоко", 42, 1f, 5f, 3.4f),
        Food(7, "Рис", 130, 0.3f, 28f, 2.7f),
        Food(8, "Гречка", 110, 1.6f, 20f, 4.2f),
        Food(9, "Помидор", 18, 0.2f, 3.9f, 0.9f),
        Food(10, "Огурец", 15, 0.1f, 3.6f, 0.7f)
    )

    fun search(query: String): List<Food> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            foods.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
}
