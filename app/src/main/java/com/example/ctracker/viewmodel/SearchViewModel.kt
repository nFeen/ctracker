package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.entity.Food
import com.example.ctracker.repositoryBack.FoodRepository
import kotlinx.coroutines.launch

class SearchViewModel(val mealType: Int) : ViewModel() {
    val query: MutableState<String> = mutableStateOf("")
    val results: MutableState<List<Food>> = mutableStateOf(emptyList())
    val hasSearched: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    private val errorMessage: MutableState<String?> = mutableStateOf(null)

    fun search() {
        if (query.value.isBlank()) {
            resetSearchState()
            return
        }

        hasSearched.value = true
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val foods = FoodRepository.searchFoods(query.value)
                results.value = foods.map {
                    Food(
                        id = it.foodId,
                        name = it.name,
                        calories = it.calorie,
                        protein = it.protein,
                        fats = it.fats,
                        carbs = it.carbs
                    )
                }
            } catch (e: Exception) {
                errorMessage.value = "Ошибка загрузки данных: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun resetSearchState() {
        hasSearched.value = false
        results.value = emptyList()
        errorMessage.value = null
    }

    fun onQuerySearch(newQuery: String) {
        query.value = newQuery
        if (newQuery.isBlank()) {
            resetSearchState()
        }
    }
}

