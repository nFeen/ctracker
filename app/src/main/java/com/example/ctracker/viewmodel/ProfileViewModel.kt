package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.SharedPreferencesManager
import com.example.ctracker.apiservice.UserProfile
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ProfileViewModel : ViewModel() {

    private val userId: Int = SharedPreferencesManager.getString("userID", "-1").toInt()

    val fats = mutableIntStateOf(SharedPreferencesManager.getInt("fats", 0))
    val carbs = mutableIntStateOf(SharedPreferencesManager.getInt("carbs", 0))
    val protein = mutableIntStateOf(SharedPreferencesManager.getInt("protein", 0))
    val maxCalorie = mutableIntStateOf(SharedPreferencesManager.getInt("maxCalorie", 2500))
    val calorie = mutableIntStateOf(SharedPreferencesManager.getInt("calorie", 0))

    val userName = mutableStateOf(SharedPreferencesManager.getString("userName", "Unknown User"))
    val userHeight = mutableIntStateOf(SharedPreferencesManager.getInt("userHeight", 0))
    val userWeight = mutableStateOf("${SharedPreferencesManager.getInt("userWeight", 0)} kg")
    val profilePic = mutableStateOf(SharedPreferencesManager.getString("profilePic", ""))
    val chartData = mutableStateOf(loadChartDataFromSharedPreferences())

    init {
        loadUserData()
        loadMacrosForToday()
        loadChartData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userProfile = UserRepository.getUserById(userId)
                if (userProfile != null) {
                    updateStateWithProfile(userProfile)
                    saveUserDataToSharedPreferences(userProfile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadMacrosForToday() {
        val currentDate = getCurrentDate()
        viewModelScope.launch {
            try {
                val meals = MealRepository.getMeals(userId, currentDate)
                if (meals.isNotEmpty()) {
                    val totalCalories = meals.sumOf { it.calories.toInt() }
                    val totalProtein = meals.sumOf { it.protein.toInt() }
                    val totalFats = meals.sumOf { it.fats.toInt() }
                    val totalCarbs = meals.sumOf { it.carbs.toInt() }

                    calorie.intValue = totalCalories
                    protein.intValue = totalProtein
                    fats.intValue = totalFats
                    carbs.intValue = totalCarbs

                    saveMacrosToSharedPreferences(
                        totalCalories,
                        totalProtein,
                        totalFats,
                        totalCarbs
                    )
                } else {
                    calorie.intValue = 0
                    protein.intValue = 0
                    fats.intValue = 0
                    carbs.intValue = 0

                    saveMacrosToSharedPreferences(0, 0, 0, 0)
                }
            } catch (e: Exception) {
                println("Ошибка при загрузке данных профиля: ${e.message}")
                calorie.intValue = 0
                protein.intValue = 0
                fats.intValue = 0
                carbs.intValue = 0

                saveMacrosToSharedPreferences(0, 0, 0, 0)
            }
        }
    }

    private fun loadChartData() {
        val savedChartData = loadChartDataFromSharedPreferences()

        val inputDateFormatter =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Формат для репозитория
        val outputDateFormatter =
            SimpleDateFormat("MM-dd", Locale.getDefault()) // Формат для графика

        // Генерация списка дат за последние 7 дней
        val dates = (0..6).map { offset ->
            val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -offset) }
            inputDateFormatter.format(calendar.time)
        }

        if (savedChartData.isEmpty()) {
            chartData.value = dates.map { date ->
                0 to outputDateFormatter.format(inputDateFormatter.parse(date)!!)
            }
        } else {
            chartData.value = savedChartData
        }
        viewModelScope.launch {
            try {
                val chartValues = mutableListOf<Pair<Int, String>>()

                for (date in dates) {
                    try {
                        // Получаем приемы пищи за указанную дату
                        val meals = MealRepository.getMeals(userId, date)
                        val totalCalories = meals.sumOf { it.calories.toInt() }

                        // Преобразуем дату для отображения на графике
                        val formattedDate =
                            outputDateFormatter.format(inputDateFormatter.parse(date)!!)
                        chartValues.add(totalCalories to formattedDate)
                    } catch (e: Exception) {
                        // Если данных за дату нет, добавляем 0 калорий
                        val formattedDate =
                            outputDateFormatter.format(inputDateFormatter.parse(date)!!)
                        chartValues.add(0 to formattedDate)
                    }
                }

                // Убедимся, что на графике ровно 7 столбиков
                chartData.value = chartValues.take(7).reversed()
                saveChartDataToSharedPreferences(chartData.value)
            } catch (e: Exception) {
                println("Ошибка загрузки данных для графика: ${e.message}")
                chartData.value = loadChartDataFromSharedPreferences()
            }
        }
    }


    private fun saveUserDataToSharedPreferences(userProfile: UserProfile) {
        SharedPreferencesManager.saveString("userName", userProfile.login)
        SharedPreferencesManager.saveInt("userWeight", userProfile.weight)
        SharedPreferencesManager.saveInt("userHeight", userProfile.height)
        SharedPreferencesManager.saveInt("maxCalorie", userProfile.calorieGoal)
        SharedPreferencesManager.saveString("profilePic", userProfile.profile_picture)
    }

    private fun saveMacrosToSharedPreferences(
        calories: Int,
        protein: Int,
        fats: Int,
        carbs: Int
    ) {
        SharedPreferencesManager.saveInt("calorie", calories)
        SharedPreferencesManager.saveInt("protein", protein)
        SharedPreferencesManager.saveInt("fats", fats)
        SharedPreferencesManager.saveInt("carbs", carbs)
    }

    private fun updateStateWithProfile(userProfile: UserProfile) {
        userName.value = userProfile.login
        userWeight.value = "${userProfile.weight} kg"
        userHeight.intValue = userProfile.height
        profilePic.value = userProfile.profile_picture
        maxCalorie.intValue = userProfile.calorieGoal
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }

    fun updateUserWeight(newWeight: Int) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateWeight(userId, newWeight)
                if (success) {
                    userWeight.value = "$newWeight kg"
                    SharedPreferencesManager.saveInt("userWeight", newWeight)
                }
            } catch (e: Exception) {
                println("Ошибка при обновлении веса: ${e.message}")
            }
        }
    }

    fun updateUserHeight(newHeight: Int) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateHeight(userId, newHeight)
                if (success) {
                    userHeight.intValue = newHeight
                    SharedPreferencesManager.saveInt("userHeight", newHeight)
                }
            } catch (e: Exception) {
                println("Ошибка при обновлении веса: ${e.message}")
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
                println("Ошибка при обновлении аватарки: ${e.message}")
            }
        }
    }

    fun updateUserCalorieGoal(newCalorieGoal: Int) {
        viewModelScope.launch {
            try {
                val success = UserRepository.updateCalorieGoal(userId, newCalorieGoal)
                if (success) {
                    maxCalorie.intValue = newCalorieGoal
                    SharedPreferencesManager.saveInt("maxCalorie", newCalorieGoal)
                }
            } catch (e: Exception) {
                println("Ошибка при обновлении цели као=лорий: ${e.message}")
            }
        }
    }

    private fun saveChartDataToSharedPreferences(chartData: List<Pair<Int, String>>) {
        val serialized = chartData.joinToString(";") { "${it.first},${it.second}" }
        SharedPreferencesManager.saveString("chartData", serialized)
    }

    private fun loadChartDataFromSharedPreferences(): List<Pair<Int, String>> {
        val serialized = SharedPreferencesManager.getString("chartData", null.toString())
        return if (serialized.isEmpty()) {
            emptyList()
        } else {
            serialized.split(";").mapNotNull { entry ->
                val parts = entry.split(",")
                if (parts.size == 2) {
                    val calories = parts[0].toIntOrNull()
                    val date = parts[1]
                    if (calories != null) {
                        calories to date
                    } else null
                } else null
            }
        }
    }
}
