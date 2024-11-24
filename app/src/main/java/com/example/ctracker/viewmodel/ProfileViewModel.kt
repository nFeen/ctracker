package com.example.ctracker.viewmodel

import SharedPreferencesManager
import UserProfile
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Получение ID пользователя из SharedPreferences
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1").toInt()

    // Макросы и калории
    val fats = mutableIntStateOf(SharedPreferencesManager.getInt("fats", 0))
    val carbs = mutableIntStateOf(SharedPreferencesManager.getInt("carbs", 0))
    val protein = mutableIntStateOf(SharedPreferencesManager.getInt("protein", 0))
    val maxCalorie = mutableIntStateOf(SharedPreferencesManager.getInt("maxCalorie", 2500))
    val calorie = mutableIntStateOf(SharedPreferencesManager.getInt("calorie", 0))

    // Логин пользователя
    val userName = mutableStateOf(SharedPreferencesManager.getString("userName", "Unknown User"))
    val userHeight = mutableStateOf(SharedPreferencesManager.getInt("userHeight", 0))
    val userWeight = mutableStateOf("${SharedPreferencesManager.getInt("userWeight", 0)} kg")
    val profilePic = mutableStateOf(SharedPreferencesManager.getString("profilePic", ""))



    // Инициализация данных пользователя
    init {
        loadUserData()
    }

    val chartData = mutableListOf(
        1870 to "20.11",
        20000 to "20.11",
        2000 to "20.11",
        1200 to "20.11",
        1800 to "20.11",
        2100 to "20.11",
        1600 to "20.11"
    )

    // Загрузка данных пользователя из репозитория
    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userProfile = UserRepository.getUserById(userId)
                if (userProfile != null) {
                    updateStateWithProfile(userProfile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно добавить обработку ошибок, например, вывод в UI
            }
        }
    }

    // Обновление веса пользователя
    fun updateUserWeight(newWeight: Int) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateWeight(userId, newWeight)
                if (success) {
                    userWeight.value = "$newWeight kg"
                    SharedPreferencesManager.saveInt("userWeight", newWeight)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно добавить обработку ошибок, например, вывод в UI
            }
        }
    }

    fun updateUserHeight(newHeight: Int) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateHeight(userId, newHeight)
                if (success) {
                    userHeight.value = newHeight
                    SharedPreferencesManager.saveInt("userHeight", newHeight)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно добавить обработку ошибок
            }
        }
    }
    fun updateUserProfilePicture(newProfilePicture: String) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateProfilePicture(userId, newProfilePicture)
                if (success) {
                    profilePic.value = newProfilePicture
                    SharedPreferencesManager.saveString("profilePic", newProfilePicture)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateStateWithProfile(userProfile: UserProfile) {
        userName.value = userProfile.login
        userWeight.value = "${userProfile.weight ?: 0} kg"
        userHeight.value = userProfile.height
        profilePic.value = userProfile.profile_picture
    }
}