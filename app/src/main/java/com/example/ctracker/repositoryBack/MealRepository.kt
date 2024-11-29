package com.example.ctracker.repositoryBack

import AddMealRequest
import EditMealRequest
import MealResponse
import com.example.ctracker.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object MealRepository {
    private val api = RetrofitClient.mealApiService

    // Получение списка приемов пищи за указанную дату
    suspend fun getMeals(userId: Int, date: String): List<MealResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMeals(userId, date).execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при получении приёмов пищи: ${e.message}")
            }
        }
    }

    // Получение информации о конкретном приеме пищи
    suspend fun getMealById(mealId: Int): MealResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMeal(mealId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при получении информации о приёме пищи: ${e.message}")
            }
        }
    }

    // Добавление нового приема пищи
    suspend fun addMeal(request: AddMealRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.addMeal(request).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при добавлении приёма пищи: ${e.message}")
            }
        }
    }

    // Редактирование приема пищи
    suspend fun editMeal(mealId: Int, quantity: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.editMeal(EditMealRequest(meal_id = mealId, quantity = quantity)).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при редактировании приёма пищи: ${e.message}")
            }
        }
    }

    // Удаление приема пищи
    suspend fun deleteMeal(mealId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteMeal(mealId = mealId).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при удалении приёма пищи: ${e.message}")
            }
        }
    }
}
