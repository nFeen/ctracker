package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.SharedPreferencesManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var loginSuccess = MutableLiveData(false)
    var errorMessage = mutableStateOf("")

    private val loginRegex = Regex("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$")

    fun onLoginChanged(newLogin: String) {
        login.value = newLogin
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onLoginClick() {
        if (!loginRegex.matches(login.value) || !passwordRegex.matches(password.value)) {
            loginSuccess.value = false
            errorMessage.value = "Логин или пароль неверные"
            return
        }

        viewModelScope.launch {
            try {
                val userId = UserRepository.authenticateUser(login.value, password.value)
                loginSuccess.value = true
                errorMessage.value = ""
                SharedPreferencesManager.saveString("userID", userId.toString())
            } catch (e: Exception) {
                loginSuccess.value = false
                errorMessage.value = "Ошибка на стороне сервера"
                println("Ошибка при входе: ${e.message}")
            }
        }
    }
}
