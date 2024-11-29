package com.example.ctracker.repositoryBack

import RegisterResponse
import UpdateCalorieGoalRequest
import UpdateHeightRequest
import UpdatePictureRequest
import UpdateWeightRequest
import UserProfile
import com.example.ctracker.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object UserRepository {
    private val api = RetrofitClient.userApiService

    // Аутентификация пользователя
    suspend fun authenticateUser(login: String, password: String): Int {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(login, password).execute()
                if (response.isSuccessful) {
                    response.body()?.user_id
                        ?: throw Exception("Не удалось получить данные пользователя")
                } else {
                    when (response.code()) {
                        401 -> throw Exception("Неверный логин или пароль")
                        else -> throw HttpException(response)
                    }
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при аутентификации: ${e.message}")
            }
        }
    }

    // Регистрация пользователя
    suspend fun addUser(login: String, password: String): RegisterResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val body = mapOf("login" to login, "password" to password)
                val response = api.register(body).execute()
                if (response.isSuccessful) {
                    response.body() ?: throw Exception("Пустой ответ от сервера")
                } else {
                    when (response.code()) {
                        409 -> null // Пользователь уже существует
                        else -> throw HttpException(response)
                    }
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при регистрации: ${e.message}")
            }
        }
    }


    // Получение профиля пользователя
    suspend fun getUserById(userId: Int): UserProfile? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProfile(userId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при получении профиля: ${e.message}")
            }
        }
    }

    // Обновление веса пользователя
    suspend fun updateWeight(userId: Int, weight: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val body = UpdateWeightRequest(userId, weight)
                val response = api.updateWeight(body).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при обновлении веса: ${e.message}")
            }
        }
    }

    // Обновление роста пользователя
    suspend fun updateHeight(userId: Int, height: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val body = UpdateHeightRequest(user_id = userId, height = height)
                val response = api.updateHeight(body).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при обновлении роста: ${e.message}")
            }
        }
    }

    // Обновление роста пользователя
    suspend fun updateProfilePicture(userId: Int, profilePicture: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val body = UpdatePictureRequest(user_id = userId, profile_picture = profilePicture)
                val response = api.updateProfilePicture(body).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при обновлении фото профиля: ${e.message}")
            }
        }
    }

    // Обновление целя калорий
    suspend fun updateCalorieGoal(userId: Int, calorieGoal: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val body = UpdateCalorieGoalRequest(user_id = userId, caloriegoal = calorieGoal)
                val response = api.updateCalorieGoal(body).execute()
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Ошибка при обновлении цели по калориям: ${e.message}")
            }
        }
    }

    // Обновление рекомендаций
    suspend fun getRecommendationPrompt(userId: Int, date: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRecommendationPrompt(userId, date).execute()
                if (response.isSuccessful) {
                    response.body()?.get("response")
                        ?: throw Exception("Не удалось получить текст рекомендации")
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                throw Exception("Ошибка при получении текста рекомендации: ${e.message}")
            }
        }
    }

}

