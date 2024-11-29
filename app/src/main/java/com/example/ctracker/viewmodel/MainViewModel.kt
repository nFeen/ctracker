package com.example.ctracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctracker.SharedPreferencesManager

class MainViewModel : ViewModel() {
    private val _logoutClicked = MutableLiveData(false)
    val logoutClicked: LiveData<Boolean> = _logoutClicked


    fun onLogoutClick() {
        _logoutClicked.value = true
        SharedPreferencesManager.saveString("userID", "-1")
        SharedPreferencesManager.saveInt("fats", 0)
        SharedPreferencesManager.saveInt("carbs", 0)
        SharedPreferencesManager.saveInt("protein", 0)
        SharedPreferencesManager.saveInt("maxCalorie", 2500)
        SharedPreferencesManager.saveInt("calorie", 0)
        SharedPreferencesManager.saveString("userName", "Unknown User")
        SharedPreferencesManager.saveInt("userHeight", 0)
        SharedPreferencesManager.saveInt("userWeight", 0)
        SharedPreferencesManager.saveString("profilePic", "")

        SharedPreferencesManager.saveString("chartData", "")
        SharedPreferencesManager.saveString("recommendation", "")
    }

    fun resetLogoutState() {
        _logoutClicked.value = false
    }
}
