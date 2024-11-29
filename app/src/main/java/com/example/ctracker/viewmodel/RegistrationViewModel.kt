package com.example.ctracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctracker.SharedPreferencesManager
import com.example.ctracker.repositoryBack.UserRepository
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var isLoginError = mutableStateOf(false)
    var isPasswordError = mutableStateOf(false)
    var loginSuccess = MutableLiveData(false)

    private val loginRegex = Regex("^[A-Za-z][A-Za-z\\d.-]{0,19}$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$")

    fun onLoginChanged(newLogin: String) {
        login.value = newLogin
        resetErrors()
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
        resetErrors()
    }

    private fun resetErrors() {
        isLoginError.value = false
        isPasswordError.value = false
        errorMessage.value = ""
    }

    fun onRegisterClick(onSuccess: () -> Unit) {
        when {
            login.value.length > 20 -> {
                isLoginError.value = true
                errorMessage.value = "Логин должен быть длиной не более 20 символов"
                return
            }
            login.value.length < 5 -> {
                isLoginError.value = true
                errorMessage.value = "Логин должен быть длиной не менее 5 символов"
                return
            }
            !loginRegex.matches(login.value) -> {
                isLoginError.value = true
                errorMessage.value =
                    "Логин должен содержать только латинские буквы, цифры, '.', или '-' и начинаться с буквы"
                return
            }
        }

        when {
            password.value.length < 4 -> {
                isPasswordError.value = true
                errorMessage.value = "Пароль должен быть длиной не менее 4 символов"
                return
            }
            !passwordRegex.matches(password.value) -> {
                isPasswordError.value = true
                errorMessage.value =
                    "Пароль должен содержать только латинские буквы, и хотя бы одну цифру"
                return
            }
        }

        viewModelScope.launch {
            try {
                val response = UserRepository.addUser(login.value, password.value)
                if (response != null) {
                    loginSuccess.value = true
                    SharedPreferencesManager.saveString("userID", response.user_id.toString())
                    onSuccess()
                } else {
                    isLoginError.value = true
                    errorMessage.value = "Пользователь уже существует"
                }
            } catch (e: Exception) {
                isLoginError.value = true
                errorMessage.value = "Ошибка регистрации: ${e.message}"
            }
        }
    }
}