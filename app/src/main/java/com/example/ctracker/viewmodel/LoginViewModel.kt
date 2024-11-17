import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.User
import com.example.ctracker.repository.mock.MockUserRepository

class LoginViewModel : ViewModel() {

    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var loginSuccess = MutableLiveData(false)
    var errorMessage = mutableStateOf("")

    private val loginRegex = Regex("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")

    fun onLoginChanged(newLogin: String) {
        login.value = newLogin
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onLoginClick() {
        if (!loginRegex.matches(login.value) or !passwordRegex.matches(password.value)) {
            loginSuccess.value = false
            errorMessage.value = "Логин или пароль неверные"
            return
        }
        // Аутентификация пользователя
        val user: User? = MockUserRepository.authenticateUser(login.value, password.value)
        if (user != null) {
            loginSuccess.value = true
            errorMessage.value = ""
            SharedPreferencesManager.saveString("UserID", user.id.toString())
        } else {
            loginSuccess.value = false
            errorMessage.value = "Логин или пароль неверные (ДЛЯ ТЕСТА, АУТЕНТИФИКАКЦИЯ НЕ ПРОШЛА)"
        }
    }
}
