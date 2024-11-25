package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Meal

import androidx.lifecycle.viewModelScope
import com.example.ctracker.repositoryBack.*
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeViewModel() : ViewModel() {

    // ID пользователя из SharedPreferences
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1")?.toInt() ?: -1

    // Максимальные калории
    val maxCalories = mutableIntStateOf(SharedPreferencesManager.getInt("maxCalorie", 2500))

    val calorie = mutableIntStateOf(SharedPreferencesManager.getInt("calorie", 0))

    // Список моделей для каждого приема пищи
    val mealList = mutableListOf<MealModel>()

    // Флаг загрузки данных
    val isLoading: MutableState<Boolean> = mutableStateOf(true)

    init {
        // Изначально создаем пустые MealModel
        initializeEmptyMealList()
        // Загружаем данные
        loadUserData()
        loadMeals()
    }

    // Инициализация пустых MealModel
    private fun initializeEmptyMealList() {
        mealList.apply {
            clear()
            add(MealModel("Завтрак", emptyList(), mutableStateOf(false)) { toggleVisibility(0) })
            add(MealModel("Обед", emptyList(), mutableStateOf(false)) { toggleVisibility(1) })
            add(MealModel("Ужин", emptyList(), mutableStateOf(false)) { toggleVisibility(2) })
            add(MealModel("Другое", emptyList(), mutableStateOf(false)) { toggleVisibility(3) })
        }
    }

    // Метод для загрузки данных о пользователе
    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = UserRepository.getUserById(userId)
                if (user != null) {
                    maxCalories.value = user.calorieGoal
                    calorie.value = 0 // Будет обновлено после загрузки приемов пищи
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Метод для загрузки данных из MealRepository
    private fun loadMeals() {
        viewModelScope.launch {
            try {
                isLoading.value = true

                // Получаем все приемы пищи пользователя
                val mealResponses = MealRepository.getMeals(userId, getCurrentDate())

                // Если данных нет, оставляем MealModel пустыми
                if (mealResponses.isEmpty()) {
                    calorie.value = 0
                    return@launch
                }

                // Преобразуем MealResponse в Meal с запросом названия еды из FoodRepository
                val userMeals = mealResponses.map { response ->
                    val foodName = try {
                        val food = FoodRepository.getFoodById(response.food_id)
                        food?.name ?: "Неизвестный продукт" // Если продукт не найден, подставляем заглушку
                    } catch (e: Exception) {
                        "Неизвестный продукт"
                    }

                    Meal(
                        id = response.meal_id,
                        mealType = response.part_of_the_day,
                        name = foodName, // Используем полученное имя продукта
                        calories = response.calories,
                        carbs = response.carbs,
                        fats = response.fats,
                        protein = response.protein,
                        quantity = response.quantity,
                        date = getCurrentDate() // Используем текущую дату или response.date, если оно есть
                    )
                }

                // Группируем приемы пищи по типу
                val breakfastProducts = userMeals.filter { it.mealType == 0 }
                val lunchProducts = userMeals.filter { it.mealType == 1 }
                val dinnerProducts = userMeals.filter { it.mealType == 2 }
                val additionalProducts = userMeals.filter { it.mealType == 3 }

                // Наполняем MealModel полученными данными
                mealList.apply {
                    clear()
                    add(MealModel("Завтрак", breakfastProducts, mutableStateOf(false)) { toggleVisibility(0) })
                    add(MealModel("Обед", lunchProducts, mutableStateOf(false)) { toggleVisibility(1) })
                    add(MealModel("Ужин", dinnerProducts, mutableStateOf(false)) { toggleVisibility(2) })
                    add(MealModel("Другое", additionalProducts, mutableStateOf(false)) { toggleVisibility(3) })
                }

                // Обновляем текущие калории
                calorie.value = userMeals.sumOf { it.calories.toInt() }
            } catch (e: Exception) {
                e.printStackTrace()
                println("ERRORMEAL $e")
                // В случае ошибки сбрасываем MealModel в пустые
                initializeEmptyMealList()
                calorie.value = 0
            } finally {
                isLoading.value = false
            }
        }
    }

    // Переключение видимости для определенного приема пищи
    private fun toggleVisibility(index: Int) {
        mealList[index].isProductListVisible.value = !mealList[index].isProductListVisible.value
    }

    // Получение текущей даты в формате yyyy-MM-dd
    private fun getCurrentDate(): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }
}


