package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    val fats = mutableIntStateOf(100)
    val carbs = mutableIntStateOf(100)
    val protein = mutableIntStateOf(100)
    val maxCalorie = mutableIntStateOf(2500)
    val calorie = mutableIntStateOf(12000)
    val chartData = mutableListOf(
        1870 to "20.11",
        20000 to "20.11",
        2000 to "20.11",
        1200 to "20.11",
        1800 to "20.11",
        2100 to "20.11",
        1600 to "20.11"
    )

    // Логин пользователя
    val userName = mutableStateOf("User123")

    // Вес пользователя
    val userWeight = mutableStateOf("75 kg")

    fun updateUserWeight(newWeight: String) {
        userWeight.value = newWeight
    }
}