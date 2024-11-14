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

    fun onLoginChanged(newLogin: String) {
        login.value = newLogin
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onLoginClick() {
        val user : User? = MockUserRepository.authenticateUser(login.value, password.value)
        if (user != null) {
            loginSuccess.value = true
            errorMessage.value = ""
            SharedPreferencesManager.saveString("UserID", user.id.toString())
        } else {
            loginSuccess.value = false
            errorMessage.value = "Invalid login or password"

        }
    }
}
