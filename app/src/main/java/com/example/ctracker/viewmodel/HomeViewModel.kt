package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Meal
import com.example.ctracker.entity.User
import com.example.ctracker.repository.mock.MockMealRepository
import com.example.ctracker.repository.mock.MockUserRepository

class HomeViewModel(
    private val mealRepository: MockMealRepository = MockMealRepository,
    private val userRepository: MockUserRepository = MockUserRepository
) : ViewModel() {

    // ID пользователя из SharedPreferences
    private val userId: Int = SharedPreferencesManager.getString("UserID", "-1").toInt()

    // Пользовательские данные
    private val user: User? = userRepository.getUserById(userId)

    // Максимальные калории
    val maxCalories: MutableState<Int> = mutableStateOf(user?.maxCalorie ?: 2000)

    // Текущие калории
    val calorie: MutableState<Int> = mutableStateOf(user?.currentCalorie ?: 0)

    // Список моделей для каждого приема пищи
    val mealList = mutableListOf<MealModel>()

    init {
        // Загружаем данные из репозитория
        loadMeals()
    }

    // Метод для загрузки данных из репозитория
    private fun loadMeals() {
        val userMeals = mealRepository.getMealsForUser(userId)

        // Разделяем продукты по категориям
        val breakfastProducts = userMeals.filter { it.mealType == 0 }
        val lunchProducts = userMeals.filter { it.mealType == 1 }
        val dinnerProducts = userMeals.filter { it.mealType == 2 }
        val additionalProducts = userMeals.filter { it.mealType == 3 }

        mealList.apply {
            clear()
            add(MealModel("Завтрак", breakfastProducts, mutableStateOf(true)) { toggleVisibility(0) })
            add(MealModel("Обед", lunchProducts, mutableStateOf(false)) { toggleVisibility(1) })
            add(MealModel("Ужин", dinnerProducts, mutableStateOf(false)) { toggleVisibility(2) })
            add(MealModel("Другое", additionalProducts, mutableStateOf(false)) { toggleVisibility(3) })
        }
    }

    // Переключение видимости для определенного приема пищи
    private fun toggleVisibility(index: Int) {
        mealList[index].isProductListVisible.value = !mealList[index].isProductListVisible.value
    }

    // Обновление текущего значения калорий
    fun updateCalories(newCalories: Int) {
        calorie.value = newCalories
        user?.currentCalorie = newCalories // Сохраняем значение в репозитории пользователя
    }
}

data class MealModel(
    val name: String, // Название приема пищи
    val productList: List<Meal>, // Список продуктов
    var isProductListVisible: MutableState<Boolean>, // Булевое значение для видимости списка продуктов
    val toggleProductList: () -> Unit // Функция для переключения видимости
)
