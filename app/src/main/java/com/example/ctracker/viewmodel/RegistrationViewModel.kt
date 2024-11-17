import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ctracker.repository.mock.MockUserRepository

class RegistrationViewModel() : ViewModel() {

    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var isLoginError = mutableStateOf(false)
    var isPasswordError = mutableStateOf(false)

    private val loginRegex = Regex("^[A-Za-z][A-Za-z\\d.-]{0,19}$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")

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
        // Проверка логина
        if (login.value.length > 20) {
            isLoginError.value = true
            errorMessage.value = "Логин должен быть длиной не более 20 символов"
            return
        }

        if (login.value.length < 5) {
            isLoginError.value = true
            errorMessage.value = "Логин должен быть длиной не менее 5 символов"
            return
        }

        if (!loginRegex.matches(login.value)) {
            isLoginError.value = true
            errorMessage.value =
                "Логин должен содержать только латинские буквы, цифры, '.', или '-' и начинаться с буквы"
            return
        }
        // Проверка пароля
        if (password.value.length < 8) {
            isPasswordError.value = true
            errorMessage.value = "Пароль должен быть длиной не менее 8 символов"
            return
        }

        if (!passwordRegex.matches(password.value)) {
            isPasswordError.value = true
            errorMessage.value =
                "Пароль должен содержать только латинские буквы, и хотя бы одну цифру"
            return
        }
        // Проверка добавления пользователя
        if (MockUserRepository.addUser(login.value, password.value)) {
            onSuccess()
        } else {
            isLoginError.value = true
            errorMessage.value = "Пользователь уже существует"
        }
    }
}
