import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ctracker.repository.mock.MockUserRepository

class RegistrationViewModel() : ViewModel(){

    var login = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")

    fun onLoginChanged(newLogin: String) {
        login.value = newLogin
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }
    fun onRegisterClick(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (MockUserRepository.addUser(login.value, password.value)) {
            onSuccess()
        } else {
            onFailure("Registration failed: User already exists.")
        }
    }
}
