package com.example.ctracker.repositoryBack

import com.example.ctracker.ApiService.FoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object FoodRepository {
    private val api = RetrofitClient.foodApiService

    // Поиск продуктов по имени
    suspend fun searchFoods(foodName: String): List<FoodResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchFoods(foodName).execute()
                if (response.isSuccessful) {
                    response.body() ?: throw Exception("Не удалось получить список продуктов")
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при поиске продуктов: ${e.message}")
            }
        }
    }

    // Получение информации о продукте по ID
    suspend fun getFoodById(foodId: Int): FoodResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFoodById(foodId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при получении информации о продукте: ${e.message}")
            }
        }
    }
}